package com.yadaniil.bitcurve.data.api.models.tx

import com.google.gson.annotations.SerializedName

/**
 * Created by danielyakovlev on 11/23/17.
 */

class BlockchainInfoTx(@SerializedName("hash") val hash: String,
                       @SerializedName("ver") val version: Int,
                       @SerializedName("weight") val weight: String,
                       @SerializedName("vin_sz") val inputsNumber: Int,
                       @SerializedName("vout_sz") val outputsNumber: Int,
                       @SerializedName("lock_time") val lockTime: Long,
                       @SerializedName("size") val sizeBytes: String,
                       @SerializedName("double_spend") val doubleSpend: Boolean,
                       @SerializedName("rbf") val isRbf: Boolean,
                       @SerializedName("time") val time: Long,
                       @SerializedName("fee") val feeSatoshi: Long,
                       @SerializedName("result") val result: Long,
                       @SerializedName("balance") val balanceSatoshi: Long,
                       @SerializedName("relayed_by") val relatedIpAddress: String,
                       @SerializedName("block_height") val blockHeight: Int,
                       @SerializedName("tx_index") val txIndex: String,
                       @SerializedName("inputs") val inputs: List<Input>,
                       @SerializedName("out") val outputs: List<Output>
)