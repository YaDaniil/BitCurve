package com.yadaniil.bitcurve.data.db

import com.yadaniil.bitcurve.data.db.models.AccountEntity
import com.yadaniil.bitcurve.data.db.models.TxEntity
import io.objectbox.android.ObjectBoxLiveData


/**
 * Created by danielyakovlev on 7/1/17.
 */
interface DbHelper {
    fun getAllAccounts(): List<AccountEntity>
    fun getAllAccountsLiveData(): ObjectBoxLiveData<AccountEntity>
    fun getAccountLiveData(accountEntityId: Long): ObjectBoxLiveData<AccountEntity>
    fun getAccountTransactionsLiveData(accountEntityId: Long): ObjectBoxLiveData<TxEntity>
    fun saveNewAccount(accountEntity: AccountEntity)
    fun getAccount(accountEntityId: Long): AccountEntity?
    fun deleteAllAccounts()
    fun updateAccount(accountEntity: AccountEntity)
}