package com.yadaniil.bitcurve.data.db.models

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

/**
 * Created by danielyakovlev on 2/20/18.
 */

@Entity
data class TxOutputEntity(
        @Id var id: Long = 0,
        val valueSatoshi: Long = 0,
        val isSpent: Boolean = false,
        val txIndex: Long = 0,
        val address: String = "",
        val type: Int = 0,
        val n: Int = 0,
        val script: String = "",

        var txEntity: ToOne<TxEntity>? = null
)