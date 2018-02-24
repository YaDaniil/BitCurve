package com.yadaniil.bitcurve.logic

import com.yadaniil.bitcurve.data.api.models.MultiaddressResponse
import com.yadaniil.bitcurve.data.api.models.UtxosResponse
import com.yadaniil.bitcurve.data.api.models.tx.BlockchainInfoTx
import com.yadaniil.bitcurve.data.db.models.*

/**
 * Created by danielyakovlev on 2/19/18.
 */

object AccountMapper {
    fun mapAccount(accountEntity: AccountEntity, response: MultiaddressResponse?): AccountEntity {
        if (response == null) return accountEntity
        return accountEntity.copy(balanceSatoshi = response.wallet.finalBalance)
    }

    fun mapTransactions(account: Account, response: MultiaddressResponse?): List<TxEntity> {
        if(response == null) return emptyList()

        val txes: MutableList<TxEntity> = ArrayList()

        response.txs.forEach {
            val tx = TxEntity(
                    hash = it.hash,
                    version = it.version,
                    weight = it.weight,
                    inputsNumber = it.inputsNumber,
                    outputsNumber = it.outputsNumber,
                    lockTime = it.lockTime,
                    sizeBytes = it.sizeBytes,
                    doubleSpend = it.doubleSpend,
                    isRbf = it.isRbf,
                    time = it.time,
                    feeSatoshi = it.feeSatoshi,
                    result = it.result,
                    balanceSatoshi = it.balanceSatoshi,
                    relatedIpAddress = it.relatedIpAddress,
                    blockHeight = it.blockHeight,
                    txIndex = it.txIndex)
            tx.account?.target = account.accountEntity

            val inputs = mapInputs(it, tx)
            tx.inputs?.clear()
            tx.inputs?.addAll(inputs)

            val outputs = mapOutputs(it, tx)
            tx.outputs?.clear()
            tx.outputs?.addAll(outputs)

            tx.isReceived = account.haveAddress(tx.getFirstReceiver())

            txes.add(tx)
        }
        return txes
    }

    private fun mapInputs(it: BlockchainInfoTx, tx: TxEntity): List<TxInputEntity> {
        val inputsEntities: MutableList<TxInputEntity> = ArrayList()
        it.inputs.forEach {
            val input = TxInputEntity(
                    sequence = it.sequence,
                    script = it.script,
                    witness = it.witness,
                    prevOutHash = it.previousOutput.hash,
                    prevOutValueSatoshi = it.previousOutput.valueSatoshi,
                    prevOutTxIndex = it.previousOutput.txIndex,
                    prevOutType = it.previousOutput.type,
                    prevOutAddress = it.previousOutput.address,
                    prevOutScript = it.previousOutput.script,
                    prevOutIsSpent = it.previousOutput.isSpent,
                    prevOutN = it.previousOutput.n)

            input.txEntity?.target = tx
            inputsEntities.add(input)
        }
        return inputsEntities
    }

    private fun mapOutputs(it: BlockchainInfoTx, tx: TxEntity): List<TxOutputEntity> {
        val outputsEntities: MutableList<TxOutputEntity> = ArrayList()
        it.outputs.forEach {
            val output = TxOutputEntity(
                    valueSatoshi = it.valueSatoshi,
                    isSpent = it.isSpent,
                    txIndex = it.txIndex,
                    address = it.address,
                    type = it.type,
                    n = it.n,
                    script = it.script)

            output.txEntity?.target = tx
            outputsEntities.add(output)
        }
        return outputsEntities
    }

    fun mapUtxo(account: Account, response: UtxosResponse): List<UtxoEntity> {
        val utxoEntities: MutableList<UtxoEntity> = ArrayList()
        response.utxos.forEach {
            val utxoEntity = UtxoEntity(
                    txHash = it.txHash,
                    txHashBigEndian = it.txHashBigEndian,
                    txIndex = it.txIndex,
                    txOutputN = it.txOutputN,
                    script = it.script,
                    value = it.value,
                    valueHex = it.valueHex,
                    confirmations = it.confirmations
            )
            utxoEntity.account?.target = account.accountEntity
            utxoEntities.add(utxoEntity)
        }
        return utxoEntities
    }
}