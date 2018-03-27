package com.yadaniil.bitcurve.data

import com.yadaniil.bitcurve.data.api.AppApiHelper
import com.yadaniil.bitcurve.data.api.BlockchainInfoService
import com.yadaniil.bitcurve.data.db.AppDbHelper
import com.yadaniil.bitcurve.data.db.DbHelper
import com.yadaniil.bitcurve.data.db.models.AccountEntity
import com.yadaniil.bitcurve.data.db.models.TxEntity
import com.yadaniil.bitcurve.data.db.models.UtxoEntity
import com.yadaniil.bitcurve.data.prefs.SharedPrefs
import com.yadaniil.bitcurve.data.prefs.SharedPrefsHelper
import org.bitcoinj.core.UTXO

/**
 * Created by danielyakovlev on 7/1/17.
 */

class Repository constructor(private val db: AppDbHelper,
                             private val sharedPrefs: SharedPrefs,
                             private val api: AppApiHelper)
    : DbHelper, SharedPrefsHelper, BlockchainInfoService {

    // region Db
    override fun getAllAccounts(): MutableList<AccountEntity> = db.getAllAccounts()

    override fun getAllAccountsLiveData() = db.getAllAccountsLiveData()

    override fun getAccountLiveData(accountEntityId: Long)
            = db.getAccountLiveData(accountEntityId)

    override fun getAccountTransactionsLiveData(accountEntityId: Long)
            = db.getAccountTransactionsLiveData(accountEntityId)

    override fun saveNewAccount(accountEntity: AccountEntity) =
            db.saveNewAccount(accountEntity)

    override fun getAccount(accountEntityId: Long) = db.getAccount(accountEntityId)

    override fun deleteAllAccounts() = db.deleteAllAccounts()

    override fun updateAccount(accountEntity: AccountEntity) = db.updateAccount(accountEntity)

    override fun getAllUtxo() = db.getAllUtxo()

    override fun getTx(txEntityId: Long) = db.getTx(txEntityId)

    override fun getAccountEntityIdOfTx(txEntityId: Long) = db.getAccountEntityIdOfTx(txEntityId)
    // endregion Db

    // region Api
    override fun downloadInfoForAddresses(addressesString: String, n: Int?, offset: Int?)
            = api.downloadInfoForAddresses(addressesString, n, offset)

    override fun downloadBlockHeight() = api.downloadBlockHeight()

    override fun downloadUtxosForAddresses(addressesString: String, limit: Int?, confirmations: Int?)
            = api.downloadUtxosForAddresses(addressesString, limit, confirmations)

    override fun pushTx(txHex: String) = api.pushTx(txHex)
    // endregion Api

    // region SharedPrefs
    override fun setMnemonicWrittenDown(boolean: Boolean) = sharedPrefs.setMnemonicWrittenDown(boolean)
    override fun isMnemonicWrittenDown() = sharedPrefs.isMnemonicWrittenDown()

    override fun saveBlockHeight(height: Int) = sharedPrefs.saveBlockHeight(height)
    override fun getBlockHeight() = sharedPrefs.getBlockHeight()
    // endregion SharedPrefs
}