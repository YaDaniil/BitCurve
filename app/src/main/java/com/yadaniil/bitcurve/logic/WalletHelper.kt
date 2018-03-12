package com.yadaniil.bitcurve.logic

import android.content.Context
import com.google.common.base.Joiner
import com.yadaniil.bitcurve.Application
import com.yadaniil.bitcurve.data.Repository
import org.bitcoinj.core.NetworkParameters
import org.bitcoinj.params.MainNetParams
import org.bitcoinj.params.TestNet3Params
import org.bitcoinj.wallet.*
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Created by danielyakovlev on 1/9/18.
 */


class WalletHelper (private val context: Context,
                    private val repo: Repository,
                    private val accountsManager: AccountsManager) {

    private val DEFAULT_WALLET_DIRECTORY_SUFFIX = "/storageWalletBTC"
    private var btcWallet: Wallet? = null
    private val directoryPath = context.filesDir.path + DEFAULT_WALLET_DIRECTORY_SUFFIX
    private val walletFile = File(directoryPath)

    // region Wallet
    fun getWallet() = btcWallet

    fun getMnemonic(): String {
        val seed = btcWallet?.keyChainSeed
        return Joiner.on(' ').join(seed?.mnemonicCode!!)
    }

    fun setupOrCreateBitcoinWallet(seed: DeterministicSeed?,
                                   walletPassword: String?): Boolean {
        if (seed != null) {
            restoreWalletFromSeed(seed)
        } else {
            // seed == null
            try {
                btcWallet = setupWalletFromFile(walletPassword)
            } catch (e: WrongPasswordException) {
                throw WrongPasswordException()
            } catch (e: UnreadableWalletException) {
                // First launch
                createNewWallet()
            }
        }

//        val walletFiles = btcWallet?.autosaveToFile(walletFile, 500, TimeUnit.MILLISECONDS, null)

        accountsManager.sync(btcWallet)
        btcWallet?.utxoProvider = UtxoProvider(repo)
        return true
    }

    fun restoreWalletFromSeed(seed: DeterministicSeed): Wallet {
        val resultWallet = createWalletFromSeed(seed)
        saveWalletToFile(resultWallet, null)
        // TODO remove app password
        return resultWallet
    }

    private fun createWalletFromSeed(seed: DeterministicSeed): Wallet {
        val keyChainGroup = KeyChainGroup(params)
        keyChainGroup.addAndActivateHDChain(
                DeterministicKeyChain(seed, DeterministicKeyChain.BIP44_ACCOUNT_ZERO_PATH))

        return Wallet(params, keyChainGroup)
    }

    private fun createNewWallet() {
        Timber.d("createNewWallet().started.")
        val keyChainGroup = KeyChainGroup(params)

        val resultWallet = Wallet(params, keyChainGroup)
        resultWallet.addAndActivateHDChain(keyChainGroup.activeKeyChain)
        saveWalletToFile(resultWallet, null)
        btcWallet = resultWallet
    }

    private fun saveWalletToFile(wallet: Wallet, password: String?) {
        Timber.d("saveWalletToFile().started")

        if (password != null && password != "") {
//            saveWalletWithPasswordToFile(wallet, password, walletFile)
        } else {
            saveWalletToFile(wallet)
        }
    }

    fun saveWalletToFile(wallet: Wallet?): Boolean {
        val directoryPath = context.filesDir.path + DEFAULT_WALLET_DIRECTORY_SUFFIX
        val walletFile = File(directoryPath)
        return try {
            wallet?.saveToFile(walletFile)
            true
        } catch (e: IOException) {
            Timber.e(e.message)
            false
        }
    }


    @Throws(UnreadableWalletException::class)
    fun setupWalletFromFile(walletPassword: String?): Wallet {

//        if (resultWallet.isEncrypted) {
//            Timber.d("setupWalletFromFile().resultWallet is encrypted.")
//            try {
//                decryptWallet(resultWallet, walletPassword, processCallback)
//            } catch (e: KeyCrypterException) {
//                Timber.e("setupWalletFromFile(). Caught KeyCrypterException==>" + e.message)
//                throw WrongPasswordException("Cannot decrypt the wallet with the provided password.")
//            }
//
//        }
        return loadBitcoinWalletFromFile()
    }

    @Throws(UnreadableWalletException::class)
    protected fun loadBitcoinWalletFromFile(): Wallet {
        Timber.d("loadBitcoinWalletFromFile().started.")
        val directoryPath = context.filesDir.path + DEFAULT_WALLET_DIRECTORY_SUFFIX
        val walletFile = File(directoryPath)
        Timber.d("loadBitcoinWalletFromFile().walletFile==>" + walletFile)

        return Wallet.loadFromFile(walletFile)
    }
    // endregion Wallet

    companion object {
        var params: NetworkParameters =
                if (Application.isTestnet) {
                    TestNet3Params.get()
                } else {
                    MainNetParams.get()
                }
    }
}