package com.yadaniil.bitcurve.logic

import com.yadaniil.bitcurve.data.Repository
import org.bitcoinj.core.*
import org.bitcoinj.core.Coin
import org.bitcoinj.script.Script
import java.math.BigInteger
import kotlin.collections.ArrayList

/**
 * Created by danielyakovlev on 1/11/18.
 */

class UtxoProvider(private val repo: Repository) : UTXOProvider {


    override fun getParams() = WalletHelper.params

    override fun getOpenTransactionOutputs(p0: MutableList<Address>?): MutableList<UTXO>? {
        val txes = repo.getAllAccounts()[0].transactions
        val utxo: MutableList<UTXO> = ArrayList()
        txes?.forEach { tx ->
            tx.outputs?.forEach { output ->
                utxo.add(
                        UTXO(Sha256Hash.wrap(tx.hash), output.n.toLong(),
                                Coin.valueOf(output.valueSatoshi),
                                tx.blockHeight,
                                false,
                                Script(BigInteger(output.script, 16).toByteArray())))
            }
        }
        return utxo
    }

    override fun getChainHeadHeight(): Int {
        return repo.getBlockHeight()
    }
}