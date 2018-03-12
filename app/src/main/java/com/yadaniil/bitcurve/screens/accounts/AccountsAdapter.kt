package com.yadaniil.bitcurve.screens.accounts

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.github.pavlospt.roundedletterview.RoundedLetterView
import com.yadaniil.bitcurve.R
import com.yadaniil.bitcurve.data.db.models.AccountEntity
import com.yadaniil.bitcurve.utils.DenominationHelper
import org.jetbrains.anko.find
import org.jetbrains.anko.onClick

/**
 * Created by danielyakovlev on 3/12/18.
 */

class AccountsAdapter(private val onAccountClick: OnAccountClick)
    : RecyclerView.Adapter<AccountsAdapter.AccountHolder>() {

    interface OnAccountClick {
        fun onClick(holder: AccountHolder, item: AccountEntity)
    }

    private val accounts: MutableList<AccountEntity> = ArrayList()

    fun setAccounts(accounts: List<AccountEntity>) {
        this.accounts.clear()
        this.accounts.addAll(accounts)
        notifyDataSetChanged()
    }

    fun getAccounts() = accounts

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AccountHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_account, parent, false)
        return AccountHolder(view)
    }

    override fun getItemCount() = accounts.size

    override fun onBindViewHolder(holder: AccountHolder?, position: Int) {
        val account = accounts[position]
        holder?.label?.text = account.label
//        holder?.description?.text = "${account?.m}-of-${account?.n}"
        holder?.balance?.text = "${DenominationHelper.satoshiToBtc(account.balanceSatoshi)} BTC"
        holder?.letterView?.titleText = account.label[0].toString()
        holder?.rootLayout?.onClick { onAccountClick.onClick(holder, account) }
    }

    class AccountHolder(view: View) : RecyclerView.ViewHolder(view) {
        var label: TextView = view.find(R.id.label)
        var description: TextView = view.find(R.id.description)
        var balance: TextView = view.find(R.id.account_balance)
        var rootLayout: LinearLayout = view.find(R.id.root_layout)
        var letterView: RoundedLetterView = view.find(R.id.rlv_name_view)

    }
}
