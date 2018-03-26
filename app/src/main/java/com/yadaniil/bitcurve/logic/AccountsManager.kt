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
        val coinType = if (Application.isTestnet) {
            ChildNumber(1, true)
        } else {
            when (accountEntity.coinType) {
                Bitcoin().name -> ChildNumber.ZERO_HARDENED
                BitcoinCash().name -> ChildNumber(BitcoinCash().coinPath, true)
                Litecoin().name -> ChildNumber(Litecoin().coinPath, true)
                else -> ChildNumber.ZERO_HARDENED
            }
        }

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
        initAddressesForAccount(account)
        accounts?.add(account)
    }

    fun getIdForNewAccount(coinType: String): Long {
        val accounts = getAllAccounts().filter { it.accountEntity.coinType == coinType }
        if (accounts.isEmpty()) return 0
        return accounts.last().accountEntity.accountId.plus(1)
    }

    fun getAccountById(accountId: Long) =
            accounts?.find { it.accountEntity.accountId == accountId }

    fun getAccountByEntityId(accountEntityId: Long) =
            accounts?.find { it.accountEntity.id == accountEntityId }


    fun deleteAllAccounts() {
        accounts?.clear()
        repo.deleteAllAccounts()
    }
    // endregion Accounts

    // region Addresses
    private fun initAddressesForAccount(account: Account) {
        val receiveAddresses: MutableList<Address> = ArrayList()
        val receiveDeterministicKeys = account.keyChain
                .getKeys(KeyChain.KeyPurpose.RECEIVE_FUNDS, INITIAL_LOOKAHEAD)
        receiveDeterministicKeys.forEach {
            receiveAddresses += it.toAddress(params)
        }

        val changeAddresses: MutableList<Address> = ArrayList()
        val changeDeterministicKeys = account.keyChain
                .getKeys(KeyChain.KeyPurpose.CHANGE, INITIAL_LOOKAHEAD)
        changeDeterministicKeys.forEach {
            changeAddresses += it.toAddress(params)
        }

        account.receiveAddresses = receiveAddresses.toList()
        account.changeAddresses = changeAddresses.toList()
    }

    fun getCurrentReceiveAddressOfAccount(accountEntityId: Long): Address? {
        val account = getAccountByEntityId(accountEntityId)
        return account?.let { it.receiveAddresses[it.accountEntity.lastIssuedReceiveAddressIndex] }
    }

    @Throws(FullGapLimitException::class)
    fun getFreshReceiveAddressOfAccount(accountEntityId: Long): Address? {
        val account = getAccountByEntityId(accountEntityId)
        val newLastIssuedReceiveAddressIndex = account?.accountEntity
                ?.lastIssuedReceiveAddressIndex?.plus(1) ?: 0

        // Check if gap limit will be exceeded
        try {
            account?.let { it.receiveAddresses[newLastIssuedReceiveAddressIndex] }
        } catch (e: IndexOutOfBoundsException) {
            throw FullGapLimitException()
        }

        account?.let {
            val updatedAccountEntity = it.accountEntity
                    .copy(lastIssuedReceiveAddressIndex = newLastIssuedReceiveAddressIndex)
            repo.updateAccount(updatedAccountEntity)
            it.accountEntity = updatedAccountEntity
        }

        return getCurrentReceiveAddressOfAccount(accountEntityId)
    }
    // endregion Addresses

}

