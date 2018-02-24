package com.yadaniil.bitcurve.data.prefs

import android.content.SharedPreferences
import com.yadaniil.bitcurve.utils.JsonUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.bitcoinj.core.UTXO
import java.util.ArrayList

/**
 * Created by danielyakovlev on 7/1/17.
 */
class SharedPrefs (private val sharedPrefs: SharedPreferences) : SharedPrefsHelper {

    // region Keys
    private val KEY_UTXO = "utxos"
    private val IS_MNEMONIC_WRITTEN_DOWN = "utxos"
    private val BLOCKCHAIN_HEIGHT = "blockchain_height"
    // endregion Keys

    override fun saveUtxo(utxo: List<UTXO>) {
        val gson = Gson()
        val listOfStrings: MutableList<String> = utxo.mapTo(ArrayList()) { gson.toJson(it) }
        saveString(KEY_UTXO, JsonUtils.toJson(listOfStrings))
    }

    override fun getUtxo(): List<UTXO> {
        val gson = Gson()
        val utxo: MutableList<UTXO> = ArrayList()
        val string = getStringByKey(KEY_UTXO)
        val listOfStrings = JsonUtils.fromJson<List<String>>(string, object : TypeToken<List<String>>() {}.type)
        for (stringUtxo in (listOfStrings ?: emptyList())) {
            utxo.add(gson.fromJson<UTXO>(string, UTXO::class.java))
        }
        return utxo
    }

    override fun isMnemonicWrittenDown() = getBooleanByKey(IS_MNEMONIC_WRITTEN_DOWN, false)
    override fun setMnemonicWrittenDown(boolean: Boolean) = saveBoolean(IS_MNEMONIC_WRITTEN_DOWN, boolean)

    override fun getBlockHeight() = getIntByKey(BLOCKCHAIN_HEIGHT, 0)

    override fun saveBlockHeight(height: Int) = saveInt(BLOCKCHAIN_HEIGHT, height)


    // region General helping methods
    private fun saveString(key: String, value: String) = sharedPrefs.edit().putString(key, value).apply()

    private fun remove(key: String) = sharedPrefs.edit().remove(key).apply()
    private fun getStringByKey(key: String): String = sharedPrefs.getString(key, "")
    private fun saveBoolean(key: String, value: Boolean) = sharedPrefs.edit().putBoolean(key, value).apply()
    private fun getBooleanByKey(key: String, default: Boolean? = null): Boolean = sharedPrefs.getBoolean(key, default
            ?: false)

    private fun saveInt(key: String, value: Int) = sharedPrefs.edit().putInt(key, value).apply()
    private fun getIntByKey(key: String, defaultValue: Int? = 0): Int = sharedPrefs.getInt(key, defaultValue
            ?: 0)

    private fun saveLong(key: String, value: Long) = sharedPrefs.edit().putLong(key, value).apply()
    private fun getLongByKey(key: String): Long = sharedPrefs.getLong(key, 0)
    // endregion General helping methods
}