package com.yadaniil.bitcurve.data.db.models

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

/**
 * Created by danielyakovlev on 2/20/18.
 */

@Entity
data class TxInputEntity(
        @Id var id: Long? = 0,
        var sequence: Long? = 0,
        var script: String? = "",
        var witness: String? = "",
        var prevOutHash: String? = "",
        var prevOutValueSatoshi: Long? = 0,
        var prevOutTxIndex: Long? = 0,
        var prevOutType: Int? = 0,
        var prevOutAddress: String? = "",
        var prevOutScript: String? = "",
        var prevOutIsSpent: Boolean = false,
        var prevOutN: Int? = 0,
        var txEntity: ToOne<TxEntity>? = null
)