package com.yadaniil.bitcurve.data.api.models

import com.google.gson.annotations.SerializedName
import com.yadaniil.bitcurve.data.api.models.tx.BlockchainInfoTx

/**
 * Created by danielyakovlev on 11/27/17.
 */

class MultiaddressResponse(@SerializedName("recommend_include_fee") val recommendIncludeFee: Boolean,
                           @SerializedName("info") val info: Info,
                           @SerializedName("wallet") val wallet: Wallet,
                           @SerializedName("addresses") val addresses: List<Address>,
                           @SerializedName("txs") val txs: List<BlockchainInfoTx>) {

    inner class Info(@SerializedName("nconnected") val nConnected: Long,
                     @SerializedName("conversion") val conversion: Double,
                     @SerializedName("symbol_local") val symbolLocal: Symbol,
                     @SerializedName("symbol_btc") val symbolBtc: Symbol,
                     @SerializedName("latest_block") val latestBlock: LatestBlock) {

        inner class Symbol(@SerializedName("code") val code: String,
                           @SerializedName("symbol") val symbol: String,
                           @SerializedName("name") val name: String,
                           @SerializedName("conversion") val conversion: Double,
                           @SerializedName("symbolAppearsAfter") val symbolAppearsAfter: Boolean,
                           @SerializedName("local") val local: Boolean)

        inner class LatestBlock(@SerializedName("block_index") val blockIndex: Long,
                                @SerializedName("height") val height: Long,
                                @SerializedName("time") val time: Long,
                                @SerializedName("prevOutHash") val hash: String)
    }

    inner class Wallet(@SerializedName("n_tx") val numberOfTx: Long,
                       @SerializedName("n_tx_filtered") val numberOfTxFiltered: Long,
                       @SerializedName("total_received") val totalReceived: Long,
                       @SerializedName("total_sent") val totalSent: Long,
                       @SerializedName("final_balance") val finalBalance: Long)

    inner class Address(@SerializedName("change_index") val changeIndex: Int,
                        @SerializedName("account_index") val accountIndex: Int,
                        @SerializedName("address") val address: String,
                        @SerializedName("n_tx") val txNumber: Int,
                        @SerializedName("total_received") val totalReceivedSatoshi: Long,
                        @SerializedName("total_sent") val totalSentSatoshi: Long,
                        @SerializedName("final_balance") val finalBalanceSatoshi: Long)

    fun getTransactions() = txs

    fun getBlockHeight() = info.latestBlock.height.toInt()
}