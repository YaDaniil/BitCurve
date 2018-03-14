package com.yadaniil.bitcurve.data.db.models

import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany
import java.util.*

/**
 * Created by danielyakovlev on 2/15/18.
 */

@Entity
data class AccountEntity(
        @Id var id: Long = 0,
        var accountId: Long = 0,
        var label: String = "",
        var balanceSatoshi: Long = 0,
        var lastIssuedReceiveAddressIndex: Int = 0,
        var lastIssuedChangeAddressIndex: Int = 0,
        var lastUsedReceiveAddressIndex: Int = 0,
        var lastUsedChangeAddressIndex: Int = 0,
        var creationTime: Date? = null,
        var coinType: String = "",

        @Backlink
        var transactions: ToMany<TxEntity>? = null,

        @Backlink
        var utxos: ToMany<UtxoEntity>? = null
)