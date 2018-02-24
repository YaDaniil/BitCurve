package com.yadaniil.bitcurve

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.yadaniil.bitcurve.di.getAllKoinModules
import org.koin.android.ext.android.startKoin
import timber.log.Timber

/**
 * Created by danielyakovlev on 1/9/18.
 */

class Application : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        startKoin(this, getAllKoinModules())

        Timber.plant(Timber.DebugTree())
    }

    companion object {
        var isTestnet = BuildConfig.BUILD_TYPE.contains("Testnet", false)
    }


}