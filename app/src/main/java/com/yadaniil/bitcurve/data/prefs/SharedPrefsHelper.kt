package com.yadaniil.bitcurve.data.prefs


/**
 * Created by danielyakovlev on 7/1/17.
 */

interface SharedPrefsHelper {

    fun isMnemonicWrittenDown(): Boolean
    fun setMnemonicWrittenDown(boolean: Boolean)

    fun saveBlockHeight(height: Int)
    fun getBlockHeight(): Int
}