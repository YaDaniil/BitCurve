package com.yadaniil.bitcurve.data.api.models.tx

import com.google.gson.annotations.SerializedName

/**
* Created by danielyakovlev on 11/28/17.
*/

class Output (
        @SerializedName("value") val valueSatoshi: Long,
        @SerializedName("spent") val isSpent: Boolean,
        @SerializedName("tx_index") val txIndex: Long,
        @SerializedName("addr") val address: String,
        @SerializedName("type") val type: Int,
        @SerializedName("n") val n: Int,
        @SerializedName("script") val script: String
)