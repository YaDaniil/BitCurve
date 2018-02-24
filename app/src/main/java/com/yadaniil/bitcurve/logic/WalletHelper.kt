package com.yadaniil.bitcurve.logic

import android.content.Context
import com.yadaniil.bitcurve.Application
import com.yadaniil.bitcurve.data.Repository
import org.bitcoinj.core.NetworkParameters
import org.bitcoinj.params.MainNetParams
import org.bitcoinj.params.TestNet3Params
import org.bitcoinj.wallet.*
import timber.log.Timber
import java.io.File
import java.io.IOException

/**
 * Created by danielyakovlev on 1/9/18.
 */


class WalletHelper (private val context: Context,
                    private val repo: Repository,
                    private val accountsManager: AccountsManager) {

    private val DEFAULT_WALLET_DIRECTORY_SUFFIX = "/storageWalletBTC"
    private var btcWallet: Wallet? = null

    // region Wallet
    fun setupOrCreateBitcoinWallet(seed: DeterministicSeed?,
                                   walletPassword: String?): Boolean {
        if (seed != null) {
            restoreWalletFromSeed(seed)
        } else {
            // seed == null
            try {
                btcWallet = setupWalletFromFile(walletPassword)
                OurWallet.btcWallet = btcWallet
            } catch (e: WrongPasswordException) {
                throw WrongPasswordException()
            } catch (e: UnreadableWalletException) {
                // First launch
                createNewWallet()
            }
        }


        accountsManager.sync()
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

    protected fun createNewWallet() {
        Timber.d("createNewWallet().started.")
        val keyChainGroup = KeyChainGroup(params)

        val resultWallet = Wallet(params, keyChainGroup)

        saveWalletToFile(resultWallet, null)
        btcWallet = resultWallet
        OurWallet.btcWallet = btcWallet
    }

    protected fun saveWalletToFile(wallet: Wallet, password: String?) {
        Timber.d("saveWalletToFile().started")

        val directoryPath = context.filesDir.path + DEFAULT_WALLET_DIRECTORY_SUFFIX
        val walletFile = File(directoryPath)

        if (password != null && password != "") {
//            saveWalletWithPasswordToFile(wallet, password, walletFile)
        } else {
            saveWalletToFile(walletFile, wallet)
        }
    }

    private fun saveWalletToFile(walletFile: File, wallet: Wallet): Boolean {
        return try {
            wallet.saveToFile(walletFile)
            true
        } catch (e: IOException) {
            e.printStackTrace()
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

    protected fun saveWalletToFileAsync() {
//        Timber.d("saveWalletToFileAsync().started.")
//        if (walletSavingTask != null) {
//            walletSavingTask.cancel(true)
//        }
//        walletSavingTask = WalletSavingTask(btcWallet, info.getCurrentWalletPassword())
//        walletSavingTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
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