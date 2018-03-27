package com.yadaniil.bitcurve.screens.tx

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.jakewharton.rxbinding2.widget.text
import com.yadaniil.bitcurve.R
import com.yadaniil.bitcurve.data.db.models.AccountEntity
import com.yadaniil.bitcurve.data.db.models.TxEntity
import com.yadaniil.bitcurve.logic.Coin
import com.yadaniil.bitcurve.utils.DateHelper
import com.yadaniil.bitcurve.utils.DenominationHelper
import com.yadaniil.bitcurve.utils.openUrl
import kotlinx.android.synthetic.main.activity_tx.*
import org.jetbrains.anko.onClick
import org.jetbrains.anko.onLongClick
import org.jetbrains.anko.toast
import org.koin.android.architecture.ext.viewModel
import java.util.*

/**
 * Created by danielyakovlev on 3/27/18.
 */

class TxActivity : AppCompatActivity() {

    private val viewModel by viewModel<TxViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tx)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val tx = viewModel.getTx(intent.getLongExtra("txEntityId", 0))
        val account = viewModel.getAccountOfTx(tx.id)
        renderTx(tx, account)
    }

    private fun renderTx(txEntity: TxEntity, accountEntity: AccountEntity?) {
        val coin = accountEntity?.getCoin()
        val confirmationsCount = txEntity.getTxConfirmationsCount(viewModel.getBlockchainHeight())
        val isTxPending = txEntity.blockHeight <= 0 || confirmationsCount < TxEntity.CONFIRMATIONS_TO_BE_CONFIRMED

        renderAmountAndDescription(txEntity, coin, isTxPending)
        renderConfirmationStatus(confirmationsCount)
        renderTxHash(txEntity)

        // Render date
        val dateAndTime = "${Date(txEntity.time * 1000)} " +
                "(${DateHelper.buildTxFriendlyDateString(txEntity, this)})"
        time_and_date.text = dateAndTime

        // Render Fee
        val feeAndFeePerByte = "${DenominationHelper.satoshiToBtc(txEntity.feeSatoshi)} " +
                "${accountEntity?.getCoin()?.ticker} " +
                "(${txEntity.feeSatoshi / txEntity.sizeBytes.toLong()} " +
                "${getString(R.string.satoshi_lowercase)}/${getString(R.string.byte_lowercase)})"
        fee.text = feeAndFeePerByte

        // Render Size
        val txSize = "${txEntity.sizeBytes} ${getString(R.string.bytes)}"
        tx_size.text = txSize

        // Render account balance after tx completed
        val balance = "${DenominationHelper.satoshiToBtc(txEntity.balanceSatoshi)} ${accountEntity?.getCoin()?.ticker}"
        account_balance.text = balance

        renderInputs(txEntity)
        renderOutputs(txEntity)
    }

    private fun renderAmountAndDescription(txEntity: TxEntity, coin: Coin?, isTxPending: Boolean) {
        val formattedAmount = if (txEntity.isReceived == true)
            "+${DenominationHelper.satoshiToBtc(txEntity.result)} ${coin?.ticker}"
        else "${DenominationHelper.satoshiToBtc(txEntity.result)} ${coin?.ticker}"

        if(txEntity.isReceived == true) {
            amount?.background= resources.getDrawable(R.drawable.amount_income_background)
            amount?.text = formattedAmount
            val txDescription = "${getString(R.string.received)} ${coin?.name} " +
            "${getString(R.string.from_lowercase)}\n ${txEntity.getSender()}"
            tx_description.text = txDescription
        } else {
            amount?.background = resources.getDrawable(R.drawable.amount_outcome_background)
            amount?.text = formattedAmount
            val txDescription = "${getString(R.string.sent)} ${coin?.name} " +
                    "${getString(R.string.to_lowercase)}\n ${txEntity.getFirstReceiver()}"
            tx_description.text = txDescription
        }
        if (isTxPending) amount?.background = resources.getDrawable(R.drawable.amount_pending_background)
    }

    private fun renderConfirmationStatus(confirmationsCount: Int) {
        val txStatus = if (confirmationsCount < 6)
            "${getString(R.string.pending)} ($confirmationsCount/6)"
        else  "${getString(R.string.confirmed)} ($confirmationsCount)"
        val fullStatus = "${getString(R.string.status)}: $txStatus"
        status.text = fullStatus
    }

    private fun renderTxHash(txEntity: TxEntity) {
        tx_hash.text = txEntity.hash
        tx_hash_layout.apply {
            onLongClick {
                val clipboardManager = getSystemService(AppCompatActivity.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("hash", tx_hash.text.toString())
                clipboardManager.primaryClip = clip
                toast(R.string.hash_copied_to_clipboard)
                true
            }
            onClick { openUrl(this@TxActivity, getString(R.string.show_tx_url, txEntity.hash)) }
        }
    }

    private fun renderInputs(txEntity: TxEntity) {
        txEntity.inputs?.forEach {
            val addresses = "${inputs.text}${it.prevOutAddress}\n"
            inputs.text = addresses
        }
    }

    private fun renderOutputs(txEntity: TxEntity) {
        txEntity.outputs?.forEach {
            val addresses = "${outputs.text}${it.address}\n"
            outputs.text = addresses
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}