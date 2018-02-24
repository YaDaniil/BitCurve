package com.yadaniil.bitcurve.logic

import com.yadaniil.bitcurve.data.Repository
import org.bitcoinj.core.Address
import org.bitcoinj.core.UTXO
import org.bitcoinj.core.UTXOProvider
import java.util.ArrayList
import java.util.HashMap

/**
 * Created by danielyakovlev on 1/11/18.
 */

class UtxoProvider(private val repo: Repository) : UTXOProvider {

    private val storage: UtxoStorage = UtxoStorage(repo)
    private var utxos: MutableList<UTXO>? = null


    @Synchronized
    fun getUtxo(): List<UTXO> {
        return utxos?.toList() ?: emptyList()
    }

    private fun getUtxoMap(): MutableMap<String, UTXO> {
        val resultMap = HashMap<String, UTXO>()

        for (utxo in (utxos ?: emptyList<UTXO>())) {
            if (utxo.hash != null) {
                val hashKey = utxo.hash.toString() + utxo.index
                resultMap.put(hashKey, utxo)
            }
        }

        return resultMap
    }

    @Synchronized
    fun mergeUTXOs(newUtxos: List<UTXO>): Boolean {
        val oldUtxos = getUtxoMap()
        var result = false

        for (curUtxo in newUtxos) {
            val key = curUtxo.hash.toString() + curUtxo.index
            if (oldUtxos[key] == null) {
                // Still no such UTXO.
                oldUtxos.put(key, curUtxo)
                result = true
            }
        }
        if (result) {
            utxos = ArrayList(oldUtxos.values)
            applyChanges()
        }
        return result
    }

    @Synchronized
    fun addUtxo(newUTXO: UTXO): Boolean {
        for (curOldUTXO in (utxos ?: emptyList<UTXO>())) {
            if (curOldUTXO == newUTXO) {
                return false
            }
        }

        utxos?.add(newUTXO)
        applyChanges()
        return true
    }

    @Synchronized
    fun removeUtxo(utxo: UTXO): Boolean {
        if (this.utxos!!.remove(utxo)) {
            applyChanges()
            return true
        } else {
            return false
        }
    }

    @Synchronized
    fun removeUtxo(utxos: List<UTXO>): Boolean {
        return if (this.utxos!!.removeAll(utxos)) {
            applyChanges()
            true
        } else {
            false
        }
    }

    @Synchronized
    fun clearUtxo() {
        utxos?.clear()
        applyChanges()
    }

    @Synchronized private fun applyChanges() {
        storage.saveUtxo(utxos ?: emptyList())
    }


    override fun getParams() = WalletHelper.params

    override fun getOpenTransactionOutputs(p0: MutableList<Address>?) = utxos

    override fun getChainHeadHeight(): Int {
        // TODO
//        repo.getLatestBlock()

        return 0
    }
}