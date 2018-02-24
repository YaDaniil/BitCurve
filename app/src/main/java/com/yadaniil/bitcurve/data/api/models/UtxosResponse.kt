package com.yadaniil.bitcurve.data.api.models

import com.google.gson.annotations.SerializedName

/**
 * Created by danielyakovlev on 2/22/18.
 */

class UtxosResponse(@SerializedName("unspent_outputs") val utxos: List<UtxoResponse>) {

    class UtxoResponse(
            @SerializedName("tx_hash") val txHash: String,
            @SerializedName("tx_hash_big_endian") val txHashBigEndian: String,
            @SerializedName("tx_index") val txIndex: String,
            @SerializedName("tx_output_n") val txOutputN: Int,
            @SerializedName("script") val script: String,
            @SerializedName("value") val value: Long,
            @SerializedName("value_hex") val valueHex: String,
            @SerializedName("confirmations") val confirmations: Int
    )
}