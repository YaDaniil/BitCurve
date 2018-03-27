package com.yadaniil.bitcurve.screens.account.main

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.yadaniil.bitcurve.R
import com.yadaniil.bitcurve.data.db.models.TxEntity
import com.yadaniil.bitcurve.utils.DateHelper
import com.yadaniil.bitcurve.utils.DenominationHelper
import org.jetbrains.anko.find
import org.jetbrains.anko.onClick

/**
 * Created by danielyakovlev on 3/12/18.
 */


class TxesAdapter(private val onTxClick: OnTxClick,
                  private val context: Context, blockchainHeight: Int)
    : RecyclerView.Adapter<TxesAdapter.TxHolder>() {

    private var blockchainHeight: Int = 0

    interface OnTxClick {
        fun onClick(holder: TxHolder, item: TxEntity)
    }

    private val txes: MutableList<TxEntity> = ArrayList()

    init {
        this.blockchainHeight = blockchainHeight
    }

    fun updateData(txes: List<TxEntity>, blockchainHeight: Int) {
        this.txes.clear()
        this.txes.addAll(txes)
        this.blockchainHeight = blockchainHeight
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TxHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tx, parent, false)
        return TxHolder(view)
    }

    override fun getItemCount() = txes.size

    override fun onBindViewHolder(holder: TxHolder, position: Int) {
        val tx = txes[position]
        val confirmationsCount = tx.getTxConfirmationsCount(blockchainHeight)
        val isTxPending = tx.blockHeight <= 0 || confirmationsCount < TxEntity.CONFIRMATIONS_TO_BE_CONFIRMED

        val formattedAmount = if (tx.isReceived == true)
            "+${DenominationHelper.satoshiToBtc(tx.result)} BTC"
        else "${DenominationHelper.satoshiToBtc(tx.result)} BTC"
        holder.time?.text = DateHelper.buildTxFriendlyDateString(tx, context)
        if(tx.isReceived == true) {
            holder.fromOrTo?.text = "${context.getString(R.string.from)} ${tx.getFirstReceiver()}"
            holder.amount?.background= context.resources.getDrawable(R.drawable.amount_income_background)
            holder.amount?.text = formattedAmount
        } else {
            holder.amount?.background = context.resources.getDrawable(R.drawable.amount_outcome_background)
            holder.fromOrTo?.text = "${context.getString(R.string.to)} ${tx.getSender()}"
            holder.amount?.text = formattedAmount
        }
        if (isTxPending) holder.amount?.background = context.resources.getDrawable(R.drawable.amount_pending_background)

        holder.itemLayout?.onClick { onTxClick.onClick(holder, tx) }
    }

    class TxHolder(view: View) : RecyclerView.ViewHolder(view) {
        val time: TextView?
        var amount: TextView? = null
        val fromOrTo: TextView?
        val itemLayout: LinearLayout?

        init {
            amount = view.find(R.id.amount)
            fromOrTo = view.find(R.id.from_to)
            time = view.find(R.id.time)
            itemLayout = view.find(R.id.tx_item_layout)
        }
    }
}