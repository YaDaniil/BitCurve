package com.yadaniil.bitcurve.screens.splash

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.yadaniil.bitcurve.BaseViewModel
import com.yadaniil.bitcurve.logic.WalletHelper
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * Created by danielyakovlev on 2/24/18.
 */


class SplashViewModel (private val walletHelper: WalletHelper) : BaseViewModel() {

    private var isWalletLoaded: MutableLiveData<Boolean>? = null


    fun loadWallet() {
        isWalletLoaded = null
        isWalletLoaded = MutableLiveData()

        Completable.fromAction {
            walletHelper.setupOrCreateBitcoinWallet(null, "")
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    isWalletLoaded?.postValue(true)
                }, {
                    isWalletLoaded?.postValue(false)
                    Timber.e(it.message)
                })
    }

    fun getIsWalletLoaded(): LiveData<Boolean>? = isWalletLoaded

}