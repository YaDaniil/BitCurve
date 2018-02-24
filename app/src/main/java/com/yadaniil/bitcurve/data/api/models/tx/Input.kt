package com.yadaniil.bitcurve.data.api.models.tx

import com.google.gson.annotations.SerializedName

/**
* Created by danielyakovlev on 11/28/17.
*/

class Input(@SerializedName("prev_out") val previousOutput: PreviousOutput,
            @SerializedName("sequence") val sequence: Long,
            @SerializedName("script") val script: String,
            @SerializedName("witness") val witness: String) {

    inner class PreviousOutput(@SerializedName("hash") val hash: String,
                               @SerializedName("value") val valueSatoshi: Long,
                               @SerializedName("tx_index") val txIndex: Long,
                               @SerializedName("type") val type: Int,
                               @SerializedName("addr") val address: String,
                               @SerializedName("script") val script: String,
                               @SerializedName("spent") val isSpent: Boolean,
                               @SerializedName("n") val n: Int)
}