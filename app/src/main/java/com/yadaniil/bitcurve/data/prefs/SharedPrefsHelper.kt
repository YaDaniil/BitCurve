package com.yadaniil.bitcurve.data.prefs

import org.bitcoinj.core.UTXO


/**
 * Created by danielyakovlev on 7/1/17.
 */

interface SharedPrefsHelper {

    fun saveUtxo(utxo: List<UTXO>)
    fun getUtxo(): List<UTXO>

    fun isMnemonicWrittenDown(): Boolean
    fun setMnemonicWrittenDown(boolean: Boolean)

    fun saveBlockHeight(height: Int)
    fun getBlockHeight(): Int
}