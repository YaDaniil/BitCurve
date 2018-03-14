package com.yadaniil.bitcurve.screens.settings.restore

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.yadaniil.bitcurve.BaseViewModel
import com.yadaniil.bitcurve.data.Repository
import com.yadaniil.bitcurve.logic.WalletHelper
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.bitcoinj.wallet.DeterministicSeed
import org.bitcoinj.wallet.Wallet
import timber.log.Timber

/**
 * Created by danielyakovlev on 2/12/18.
 */

class MnemonicRestoreViewModel(private val walletHelper: WalletHelper,
                               private val repo: Repository) : BaseViewModel() {

    private var isWalletRestoredLiveData: MutableLiveData<Boolean>? = null

    lateinit var seed: DeterministicSeed
    lateinit var wallet: Wallet

    fun restoreWallet(mnemonic: String): LiveData<Boolean> {
        isWalletRestoredLiveData = MutableLiveData()

        Completable.fromAction {
            seed = DeterministicSeed(mnemonic, null, "", 0)
            wallet = walletHelper.restoreWalletFromSeed(seed)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    walletHelper.setWallet(wallet)
                    repo.deleteAllAccounts()
                    isWalletRestoredLiveData?.postValue(true)
                }, {
                    isWalletRestoredLiveData?.postValue(false)
                    Timber.e(it.message)
                })

        return isWalletRestoredLiveData ?: MutableLiveData()
    }


}