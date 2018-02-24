package com.yadaniil.bitcurve.data.db.models

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

/**
 * Created by danielyakovlev on 2/23/18.
 */

@Entity
data class UtxoEntity(
        @Id var id: Long = 0,
        val txHash: String = "",
        val txHashBigEndian: String = "",
        val txIndex: String = "",
        val txOutputN: Int = 0,
        var script: String = "",
        val value: Long = 0,
        val valueHex: String = "",
        val confirmations: Int = 0,
        var account: ToOne<AccountEntity>? = null
)