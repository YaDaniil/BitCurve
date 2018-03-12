package com.yadaniil.bitcurve.screens.accounts

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.yadaniil.bitcurve.BaseViewModel
import com.yadaniil.bitcurve.data.Repository
import com.yadaniil.bitcurve.data.api.models.MultiaddressResponse
import com.yadaniil.bitcurve.data.db.models.AccountEntity
import com.yadaniil.bitcurve.logic.Account
import com.yadaniil.bitcurve.logic.AccountMapper
import com.yadaniil.bitcurve.logic.AccountsManager
import com.yadaniil.bitcurve.logic.WalletHelper
import io.objectbox.android.ObjectBoxLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import timber.log.Timber
import java.util.*

/**
 * Created by danielyakovlev on 3/12/18.
 */

class AccountsViewModel(private var repo: Repository,
                        private var accountsManager: AccountsManager,
                        private var walletHelper: WalletHelper) : BaseViewModel() {

    private lateinit var accountsLiveData: ObjectBoxLiveData<AccountEntity>
    private lateinit var isMnemonicWrittenDown: MutableLiveData<Boolean>
    private lateinit var isPullToRefreshLoading: MutableLiveData<Boolean>

    fun getAccountById(id: Long) = accountsManager.getAccountById(id)

    fun getAccountLiveData(): ObjectBoxLiveData<AccountEntity> {
        accountsLiveData = repo.getAllAccountsLiveData()
        return accountsLiveData
    }

    fun getIsMnemonicWrittenDown(): LiveData<Boolean> {
        isMnemonicWrittenDown = MutableLiveData()
        isMnemonicWrittenDown.postValue(repo.isMnemonicWrittenDown())
        return isMnemonicWrittenDown
    }

    fun setMnemonicWrittenDown(boolean: Boolean) {
        repo.setMnemonicWrittenDown(boolean)
        isMnemonicWrittenDown.postValue(boolean)
    }

    fun deleteAllAccounts() = accountsManager.deleteAllAccounts()

    fun getMnemonic() = walletHelper.getMnemonic()

    fun getLoadingStateLiveData(): LiveData<Boolean> {
        isPullToRefreshLoading = MutableLiveData()
        return isPullToRefreshLoading
    }

    fun updateAll(accountsEntities: List<AccountEntity>) {
        val accounts: MutableList<Account?> = ArrayList()
        accountsEntities.mapTo(accounts) { accountsManager.getAccountById(it.accountId) }

        repo.downloadBlockHeight()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    repo.saveBlockHeight(it)
                    Timber.d("Current height saved: $it")
                    updateAccounts(accounts)
                }, {
                    Timber.e(it.message)
                })
    }

    private fun updateAccounts(accounts: List<Account?>) {
        var accountUpdated = 0
        accounts.forEach { account ->
            account?.let { currentAccount ->
                val addressesString = currentAccount.stringifyAddresses()
                var multiaddressResponse: MultiaddressResponse? = null
                repo.downloadInfoForAddresses(addressesString)
                        .subscribeOn(Schedulers.io())
                        .flatMap {
                            multiaddressResponse = it
                            repo.downloadUtxosForAddresses(addressesString)
                        }
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnComplete {
                            // Stop refresh animation only after all accounts were updated
                            accountUpdated++
                            if (accountUpdated == accounts.size) {
                                isPullToRefreshLoading.postValue(false)
                            }
                        }
                        .doOnError {
                            if (it?.message?.contains("HTTP 500") == true) {
                                // There is no utxo for account
                                accountUpdated++
                                if (accountUpdated == accounts.size) {
                                    isPullToRefreshLoading.postValue(false)
                                }
                            }
                        }
                        .subscribe({ utxoResponse ->
                            doAsync {
                                val updatedAccount = AccountMapper
                                        .mapAccount(currentAccount, multiaddressResponse, utxoResponse)

                                repo.updateAccount(updatedAccount)
                                Timber.e("Balance: ${multiaddressResponse?.wallet?.finalBalance}")
                            }
                        }, { error ->
                            Timber.e(error.message)
                            // error = "Error updating account "Label"
                        })
            }
        }
    }

}