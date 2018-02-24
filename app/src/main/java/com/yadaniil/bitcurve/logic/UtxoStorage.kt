package com.yadaniil.bitcurve.logic

import com.yadaniil.bitcurve.data.Repository
import org.bitcoinj.core.UTXO

/**
 * Created by danielyakovlev on 1/11/18.
 */
class UtxoStorage (private val repo: Repository) {

    private var isBalanceChangeEventEnabled = true
    private var anounceBalanceChangeRequested = false

    @Synchronized
    fun getUtxo(): List<UTXO> {
        return repo.getUtxo()
    }

    @Synchronized
    fun saveUtxo(utxo: List<UTXO>) {
        repo.saveUtxo(utxo)
        tryToAnounceBalanceChange()
    }

    private fun tryToAnounceBalanceChange() {
        if (isBalanceChangeEventEnabled) {
            announceBalanceChange()
        } else {
            anounceBalanceChangeRequested = true
        }
    }


    private fun announceBalanceChange() {
        anounceBalanceChangeRequested = false
//        WalletHelperFactory.getWalletHelper().refreshUTXOBasedBalance()
//        DownloadingListenersManager.getInstance().initBalanceChangedEvent()
    }

    /**
     * Allow you to temporarily disable firing balance change (e.g. to avoid balance blinking).
     * DONT FORGET TO TURN IT BACK ON!
     */
    @Synchronized
    fun setBalanceChangeEventEnabled(isEnabled: Boolean) {
        if (isEnabled && !isBalanceChangeEventEnabled && anounceBalanceChangeRequested) {
            announceBalanceChange()
        }

        isBalanceChangeEventEnabled = isEnabled
    }

}