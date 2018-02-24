package com.yadaniil.bitcurve.data

import com.yadaniil.bitcurve.data.api.AppApiHelper
import com.yadaniil.bitcurve.data.api.BlockchainInfoService
import com.yadaniil.bitcurve.data.db.AppDbHelper
import com.yadaniil.bitcurve.data.db.DbHelper
import com.yadaniil.bitcurve.data.db.models.AccountEntity
import com.yadaniil.bitcurve.data.prefs.SharedPrefs
import com.yadaniil.bitcurve.data.prefs.SharedPrefsHelper
import org.bitcoinj.core.UTXO

/**
 * Created by danielyakovlev on 7/1/17.
 */

class Repository constructor(private val appDbHelper: AppDbHelper,
                             private val sharedPrefs: SharedPrefs,
                             private val appApiHelper: AppApiHelper)
    : DbHelper, SharedPrefsHelper, BlockchainInfoService {

    // region Db
    override fun getAllAccounts(): MutableList<AccountEntity> = appDbHelper.getAllAccounts()

    override fun getAllAccountsLiveData() = appDbHelper.getAllAccountsLiveData()

    override fun getAccountLiveData(accountEntityId: Long)
            = appDbHelper.getAccountLiveData(accountEntityId)

    override fun getAccountTransactionsLiveData(accountEntityId: Long)
            = appDbHelper.getAccountTransactionsLiveData(accountEntityId)

    override fun saveNewAccount(accountEntity: AccountEntity) =
            appDbHelper.saveNewAccount(accountEntity)

    override fun getAccount(accountEntityId: Long) = appDbHelper.getAccount(accountEntityId)

    override fun deleteAllAccounts() = appDbHelper.deleteAllAccounts()

    override fun updateAccount(accountEntity: AccountEntity) = appDbHelper.updateAccount(accountEntity)
    // endregion Db

    // region Api
    override fun downloadInfoForAddresses(addressesString: String, n: Int?, offset: Int?)
            = appApiHelper.downloadInfoForAddresses(addressesString, n, offset)

    override fun downloadBlockHeight() = appApiHelper.downloadBlockHeight()

    override fun downloadUtxosForAddresses(addressesString: String, limit: Int?, confirmations: Int?)
            = appApiHelper.downloadUtxosForAddresses(addressesString, limit, confirmations)
    // endregion Api

    // region SharedPrefs
    override fun saveUtxo(utxo: List<UTXO>) = sharedPrefs.saveUtxo(utxo)

    override fun getUtxo() = sharedPrefs.getUtxo()

    override fun setMnemonicWrittenDown(boolean: Boolean) = sharedPrefs.setMnemonicWrittenDown(boolean)
    override fun isMnemonicWrittenDown() = sharedPrefs.isMnemonicWrittenDown()

    override fun saveBlockHeight(height: Int) = sharedPrefs.saveBlockHeight(height)
    override fun getBlockHeight() = sharedPrefs.getBlockHeight()
    // endregion SharedPrefs
}