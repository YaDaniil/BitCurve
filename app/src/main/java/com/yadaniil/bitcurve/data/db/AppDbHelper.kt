package com.yadaniil.bitcurve.data.db

import com.yadaniil.bitcurve.data.db.models.*
import io.objectbox.Box
import io.objectbox.android.ObjectBoxLiveData

/**
 * Created by danielyakovlev on 7/1/17.
 */

class AppDbHelper(private val accountBox: Box<AccountEntity>,
                  private val transactionBox: Box<TxEntity>,
                  private val utxoBox: Box<UtxoEntity>) : DbHelper {

    override fun getAllAccounts(): MutableList<AccountEntity> = accountBox.all

    override fun getAllAccountsLiveData() = ObjectBoxLiveData(accountBox.query()
            .order(AccountEntity_.creationTime).build())

    override fun getAccountLiveData(accountEntityId: Long) =
            ObjectBoxLiveData(accountBox.query().equal(AccountEntity_.id, accountEntityId).build())

    override fun getAccountTransactionsLiveData(accountEntityId: Long) =
            ObjectBoxLiveData(transactionBox.query().equal(TxEntity_.accountId, accountEntityId).build())

    override fun saveNewAccount(accountEntity: AccountEntity) {
        accountBox.put(accountEntity)
    }

    override fun getAccount(accountEntityId: Long): AccountEntity? {
        return accountBox.query().equal(AccountEntity_.id, accountEntityId).build().findFirst()
    }

    override fun deleteAllAccounts() {
        accountBox.removeAll()
        transactionBox.removeAll()
    }

    override fun updateAccount(accountEntity: AccountEntity) {
        accountBox.put(accountEntity)
    }

    override fun getAllUtxo(): MutableList<UtxoEntity> = utxoBox.all

    override fun getTx(txEntityId: Long): TxEntity = transactionBox.get(txEntityId)

    override fun getAccountEntityIdOfTx(txEntityId: Long) = transactionBox.get(txEntityId).account?.targetId ?: 0
}