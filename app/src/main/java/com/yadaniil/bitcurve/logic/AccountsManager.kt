package com.yadaniil.bitcurve.logic

import com.yadaniil.bitcurve.Application
import com.yadaniil.bitcurve.data.Repository
import com.yadaniil.bitcurve.data.db.models.AccountEntity
import com.google.common.collect.ImmutableList
import org.bitcoinj.core.Address
import org.bitcoinj.core.NetworkParameters
import org.bitcoinj.crypto.ChildNumber
import org.bitcoinj.wallet.DeterministicKeyChain
import org.bitcoinj.wallet.KeyChain
import org.bitcoinj.wallet.Wallet

/**
 * Created by danielyakovlev on 1/29/18.
 */

class AccountsManager(private val repo: Repository) {

    private var accounts: MutableList<Account>? = null
    private var params: NetworkParameters

    private val INITIAL_LOOKAHEAD = 20

    init {
        accounts = ArrayList()
        params = WalletHelper.params
    }

    fun sync(btcWallet: Wallet?) {
        repo.getAllAccounts().forEach { addAccount(it, btcWallet) }
    }

    // region Accounts
    fun getAllAccounts() = accounts?.toList() ?: emptyList()

    fun addAccount(accountEntity: AccountEntity, wallet: Wallet?) {
        val coinType = if (Application.isTestnet)
            ChildNumber(1, true)
        else ChildNumber.ZERO_HARDENED

        val accountPath = ImmutableList.of(
                ChildNumber(44, true),
                coinType,
                ChildNumber(accountEntity.accountId.toInt(), true))

        val keyChain = DeterministicKeyChain(wallet?.keyChainSeed, accountPath)
        wallet?.addAndActivateHDChain(keyChain)
        addAccount(accountEntity, keyChain)
    }

    private fun addAccount(accountEntity: AccountEntity, keyChain: DeterministicKeyChain) {
        if (keyChain.lookaheadSize < accountEntity.lastIssuedReceiveAddressIndex) {
            keyChain.lookaheadSize = accountEntity.lastIssuedReceiveAddressIndex
            keyChain.maybeLookAhead()
        }

        val account = Account(accountEntity, keyChain)
        initReceiveAddressesForAccount(account)
        accounts?.add(account)
    }

    fun getIdForNewAccount(): Long {
        if (accounts == null || accounts!!.isEmpty()) return 0
        return accounts?.last()?.accountEntity?.accountId?.plus(1) ?: 0
    }

    fun getAccountById(accountId: Long): Account? {
        accounts?.forEach {
            if (it.accountEntity.accountId == accountId)
                return it
        }
        return null
    }

    fun getAccountByEntityId(accountEntityId: Long): Account? {
        accounts?.forEach {
            if (it.accountEntity.id == accountEntityId)
                return it
        }
        return null
    }

    fun deleteAllAccounts() {
        accounts?.clear()
        repo.deleteAllAccounts()
    }
    // endregion Accounts

    // region Addresses
    private fun initAddresses() {
        accounts?.forEach {
            initReceiveAddressesForAccount(it)
        }
    }

    fun getCurrentReceiveAddressForAccount(accountId: Long) =
            getAccountById(accountId)?.accountEntity?.currentReceiveAddress

    fun getCurrentChangeAddressForAccount(accountId: Long) =
            getAccountById(accountId)?.accountEntity?.currentChangeAddress

    fun getFreshReceiveAddressForAccount(accountId: Long): String {
        repo.getAllAccounts()
        return getAccountById(accountId)?.accountEntity?.currentReceiveAddress ?: ""
    }

    private fun initReceiveAddressesForAccount(account: Account) {
        val receiveAddresses: MutableList<Address> = ArrayList()
        val receiveDeterministicKeys = account.keyChain.getKeys(KeyChain.KeyPurpose.RECEIVE_FUNDS, 20)
        receiveDeterministicKeys.forEach {
            receiveAddresses += it.toAddress(params)
        }

        val changeAddresses: MutableList<Address> = ArrayList()
        val changeDeterministicKeys = account.keyChain.getKeys(KeyChain.KeyPurpose.CHANGE, 20)
        changeDeterministicKeys.forEach {
            changeAddresses += it.toAddress(params)
        }

        account.receiveAddresses = receiveAddresses.toList()
        account.changeAddresses = changeAddresses.toList()
    }
    // endregion Addresses



}

