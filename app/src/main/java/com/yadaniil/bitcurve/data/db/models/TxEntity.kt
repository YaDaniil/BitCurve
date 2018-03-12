package com.yadaniil.bitcurve.data.db.models

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany
import io.objectbox.relation.ToOne

/**
 * Created by danielyakovlev on 2/19/18.
 */

@Entity
data class TxEntity(
        @Id var id: Long = 0,
        var hash: String = "",
        val version: Int = 0,
        val weight: String = "",
        val inputsNumber: Int = 0,
        val outputsNumber: Int = 0,
        val lockTime: Long = 0,
        val sizeBytes: String = "",
        val doubleSpend: Boolean = false,
        val isRbf: Boolean = false,
        val time: Long = 0,
        val feeSatoshi: Long = 0,
        val result: Long = 0,
        val balanceSatoshi: Long = 0,
        val relatedIpAddress: String = "",
        val blockHeight: Int = 0,
        val txIndex: String = "",

        var account: ToOne<AccountEntity>? = null,

        var inputs: ToMany<TxInputEntity>? = null,
        var outputs: ToMany<TxOutputEntity>? = null,

        var isReceived: Boolean? = null
) {

    fun getFirstReceiver(): String = outputs?.first()?.address ?: ""

    fun getReceivers(): List<String> {
        val receivers: MutableList<String> = ArrayList()
        outputs?.mapTo(receivers) { it.address }
        return receivers
    }

    fun getSender(): String = inputs?.first()?.prevOutAddress ?: ""

    fun getSenders(): List<String> {
        val senders: MutableList<String> = ArrayList()
        inputs?.mapTo(senders) { it.prevOutAddress ?: "" }
        return senders
    }

    fun getTxConfirmationsCount(latestBlock: Int) =
            if (blockHeight <= 0) 0 else Math.max(latestBlock, blockHeight) - blockHeight + 1

    companion object {
        val CONFIRMATIONS_TO_BE_CONFIRMED = 6
        val REJECTED_TRANSACTION_HEIGHT = -3
    }

    fun isRejected() = blockHeight == REJECTED_TRANSACTION_HEIGHT

    fun isOutgoing() = !(isReceived ?: false)
}