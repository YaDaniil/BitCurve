package com.yadaniil.bitcurve.di

import android.os.Environment
import android.preference.PreferenceManager
import com.yadaniil.bitcurve.data.api.BlockchainInfoService
import com.yadaniil.bitcurve.logic.AccountsManager
import com.yadaniil.bitcurve.logic.WalletHelper
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.yadaniil.bitcurve.BuildConfig
import com.yadaniil.bitcurve.data.Repository
import com.yadaniil.bitcurve.data.api.AppApiHelper
import com.yadaniil.bitcurve.data.db.AppDbHelper
import com.yadaniil.bitcurve.data.db.models.AccountEntity
import com.yadaniil.bitcurve.data.db.models.MyObjectBox
import com.yadaniil.bitcurve.data.db.models.TxEntity
import com.yadaniil.bitcurve.data.db.models.UtxoEntity
import com.yadaniil.bitcurve.data.prefs.SharedPrefs
import com.yadaniil.bitcurve.screens.account.main.MainViewModel
import com.yadaniil.bitcurve.screens.account.receive.ReceiveViewModel
import com.yadaniil.bitcurve.screens.account.send.SendViewModel
import com.yadaniil.bitcurve.screens.accounts.AccountsViewModel
import com.yadaniil.bitcurve.screens.splash.SplashViewModel
import io.objectbox.BoxStore
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.applicationContext
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by danielyakovlev on 2/23/18.
 */

fun getAllKoinModules() = listOf(appModule, netModule, dbModule, viewModelModule)

val appModule = applicationContext {
    bean { PreferenceManager.getDefaultSharedPreferences(androidApplication()) }
}

val dbModule = applicationContext {

    // ObjectBox
    bean { MyObjectBox.builder().androidContext(androidApplication()).build() }

    bean { AppApiHelper(get()) }
    bean { SharedPrefs(get()) }
    bean {
        AppDbHelper(
                get<BoxStore>().boxFor(AccountEntity::class.java),
                get<BoxStore>().boxFor(TxEntity::class.java),
                get<BoxStore>().boxFor(UtxoEntity::class.java)) }
    bean { Repository(get(), get(), get()) }

    bean { AccountsManager(get()) }
    bean { WalletHelper(androidApplication(), get(), get()) }
}

val viewModelModule = applicationContext {
    viewModel { SplashViewModel(get()) }
    viewModel { ReceiveViewModel(get()) }
    viewModel { AccountsViewModel(get(), get(), get()) }
    viewModel { MainViewModel(get(), get()) }
    viewModel { SendViewModel(get(), get(), get()) }
}

val netModule = applicationContext {
    bean {
        createWebService<BlockchainInfoService>(createOkHttpClient(),
                BuildConfig.BLOCKCHAIN_INFO_URL)
    }
}

fun createOkHttpClient(): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
    val cache = Cache(Environment.getDownloadCacheDirectory(), 10 * 1024 * 1024)
    return OkHttpClient.Builder()
            .connectTimeout(60L, TimeUnit.SECONDS)
            .readTimeout(60L, TimeUnit.SECONDS)
            .cache(cache)
            .addInterceptor(httpLoggingInterceptor).build()
}

inline fun <reified T> createWebService(okHttpClient: OkHttpClient, url: String): T {
    val gsonBuilder = GsonBuilder()
    val gson = gsonBuilder.setFieldNamingPolicy(
            FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setLenient().create()

    val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    return retrofit.create(T::class.java)
}