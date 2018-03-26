package com.yadaniil.bitcurve.screens.add

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.yadaniil.bitcurve.BaseViewModel
import com.yadaniil.bitcurve.data.Repository
import com.yadaniil.bitcurve.data.db.models.AccountEntity
import com.yadaniil.bitcurve.logic.AccountsManager
import com.yadaniil.bitcurve.logic.WalletHelper
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.*

/**
 * Created by danielyakovlev on 3/16/18.
 */

class AddAccountViewModel(private val accountsManager: AccountsManager,
                          private val walletHelper: WalletHelper,
                          private val repo: Repository) : BaseViewModel() {

    private var isAccountCreatedLiveData: MutableLiveData<Boolean>? = null

    fun createAccount(accountLabel: String, coinType: String): LiveData<Boolean> {
        isAccountCreatedLiveData = MutableLiveData()

        Completable.fromAction {
            val accountEntity = AccountEntity(accountId = accountsManager.getIdForNewAccount(), label = accountLabel,
                    creationTime = Date(), coinType = coinType)
            repo.saveNewAccount(accountEntity)
            accountsManager.addAccount(accountEntity, walletHelper.getWallet())
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    isAccountCreatedLiveData?.postValue(true)
                }, {
                    isAccountCreatedLiveData?.postValue(false)
                    Timber.e(it.message)
                })

        return isAccountCreatedLiveData ?: MutableLiveData()
    }


}