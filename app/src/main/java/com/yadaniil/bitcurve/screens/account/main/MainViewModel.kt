package com.yadaniil.bitcurve.screens.account.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.yadaniil.bitcurve.BaseViewModel
import com.yadaniil.bitcurve.data.api.models.MultiaddressResponse
import com.yadaniil.bitcurve.data.db.models.AccountEntity
import com.yadaniil.bitcurve.data.db.models.TxEntity
import com.yadaniil.bitcurve.logic.AccountMapper
import io.objectbox.android.ObjectBoxLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import timber.log.Timber
import com.yadaniil.bitcurve.data.Repository
import com.yadaniil.bitcurve.logic.AccountsManager

/**
 * Created by danielyakovlev on 3/12/18.
 */

class MainViewModel(private val repo: Repository,
                    private val accountsManager: AccountsManager) : BaseViewModel() {

    private lateinit var account: ObjectBoxLiveData<AccountEntity>
    private lateinit var txes: ObjectBoxLiveData<TxEntity>
    private lateinit var isPullToRefreshLoading: MutableLiveData<Boolean>

    fun getBlockchainHeight(): Int = repo.getBlockHeight()

    fun getAccountLiveData(accountEntityId: Long): ObjectBoxLiveData<AccountEntity> {
        account = repo.getAccountLiveData(accountEntityId)
        return account
    }

    fun getAccountTransactionsLiveData(accountId: Long): ObjectBoxLiveData<TxEntity> {
        txes = repo.getAccountTransactionsLiveData(accountId)
        return txes
    }

    fun getPullToRefreshStateLiveData(): LiveData<Boolean> {
        isPullToRefreshLoading = MutableLiveData()
        return isPullToRefreshLoading
    }

    fun updateSingleAccount(accountEntityId: Long?) {
        repo.downloadBlockHeight()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    repo.saveBlockHeight(it)
                    updateAccount(accountEntityId)
                }, {

                })
    }

    private fun updateAccount(accountEntityId: Long?) {
        val currentAccount = accountsManager.getAccountById(
                repo.getAccount(accountEntityId ?: 0)?.accountId ?: 0)

        val addressesString = currentAccount?.stringifyAddresses() ?: ""
        var multiaddressResponse: MultiaddressResponse? = null

        repo.downloadInfoForAddresses(addressesString)
                .subscribeOn(Schedulers.io())
                .flatMap {
                    multiaddressResponse = it
                    repo.downloadUtxosForAddresses(addressesString)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {
                    isPullToRefreshLoading.postValue(false)
                }
                .doOnError {
                    if (it?.message?.contains("HTTP 500") == true) {
                        // There is no utxo for account
                        isPullToRefreshLoading.postValue(false)
                    }
                }
                .subscribe({ utxosResponse ->
                    doAsync {
                        val updatedAccount = AccountMapper
                                .mapAccount(currentAccount!!, multiaddressResponse, utxosResponse)

                        repo.updateAccount(updatedAccount)
                        Timber.e("Balance: ${multiaddressResponse?.wallet?.finalBalance}")
                    }
                }, { error ->
                    Timber.e(error.message)
                    // error = "Error updating account "Label"
                })
    }

}