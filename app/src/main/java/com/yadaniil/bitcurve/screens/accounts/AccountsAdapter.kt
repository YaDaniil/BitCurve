package com.yadaniil.bitcurve.screens.accounts

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.yadaniil.bitcurve.R
import com.yadaniil.bitcurve.data.db.models.AccountEntity
import com.yadaniil.bitcurve.logic.Bitcoin
import com.yadaniil.bitcurve.logic.BitcoinCash
import com.yadaniil.bitcurve.logic.Litecoin
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
        holder?.description?.text = account.coinType
        holder?.balance?.text = "${DenominationHelper.satoshiToBtc(account.balanceSatoshi)} BTC"

        when (account.coinType) {
            Bitcoin().name -> holder?.icon?.setImageResource(R.drawable.ic_btc)
            BitcoinCash().name -> holder?.icon?.setImageResource(R.drawable.ic_bch_green)
            Litecoin().name -> holder?.icon?.setImageResource(R.drawable.ic_ltc)
        }

        holder?.rootLayout?.onClick { onAccountClick.onClick(holder, account) }
    }

    class AccountHolder(view: View) : RecyclerView.ViewHolder(view) {
        var label: TextView = view.find(R.id.label)
        var description: TextView = view.find(R.id.description)
        var balance: TextView = view.find(R.id.account_balance)
        var rootLayout: LinearLayout = view.find(R.id.root_layout)
        var icon: ImageView = view.find(R.id.coin_icon)

    }
}
