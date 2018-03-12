package com.yadaniil.bitcurve.screens.account.main

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.yadaniil.bitcurve.R
import com.yadaniil.bitcurve.data.db.models.TxEntity
import com.yadaniil.bitcurve.utils.DenominationHelper
import com.yadaniil.bitcurve.utils.visible
import kotlinx.android.synthetic.main.content_scrolling.*
import kotlinx.android.synthetic.main.fragment_main.*
import org.bitcoinj.core.Coin
import org.jetbrains.anko.toast
import org.koin.android.architecture.ext.viewModel

/**
 * Created by danielyakovlev on 2/24/18.
 */

class MainFragment : Fragment(), TxesAdapter.OnTxClick {

    private val viewModel by viewModel<MainViewModel>()
    private lateinit var txesAdapter: TxesAdapter
    private var accountEntityId: Long? = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View
            = inflater.inflate(R.layout.fragment_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        initToolbar()
        initTxList()
        initPullToRefresh()
    }

    private fun initToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp)
        viewModel.getAccountLiveData(accountEntityId ?: 0).observe(this, Observer {
            val account = it?.first()
            wallet_name_text_view.text = account?.label
            collapsing_toolbar.title = DenominationHelper.satoshiToBtc(
                    Coin.valueOf(account?.balanceSatoshi ?: 0)).toPlainString() + " BTC"
        })
    }

    private fun initTxList() {
        txesAdapter = TxesAdapter(this, activity!!, viewModel.getBlockchainHeight())
        txes_list.layoutManager = LinearLayoutManager(activity)
        txes_list.setHasFixedSize(true)
        txes_list.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        txes_list.adapter = txesAdapter

        viewModel.getAccountTransactionsLiveData(accountEntityId ?: 0)
                .observe(this, Observer {
                    it?.let {
                        if (it.isNotEmpty()) {
                            showTransactionsList()
                            txesAdapter.updateData(it, viewModel.getBlockchainHeight())
                        } else {
                            showNoTransactionsView()
                        }
                    }
                })
    }

    private fun initPullToRefresh() {
        pullToRefresh.setOnRefreshListener {
            viewModel.updateSingleAccount(accountEntityId)
        }

        viewModel.getPullToRefreshStateLiveData().observe(this, Observer {
            it?.let { isLoading ->
                if(isLoading) showPullToRefreshLoading() else stopPullToRefreshLoading()
            }
        })
    }

    override fun onClick(holder: TxesAdapter.TxHolder, item: TxEntity) {
//        Navigator.toTxInfoActivity(item.id, this)
    }

    private fun showTransactionsList() {
        txes_list.visible = true
        empty_view_layout.visible = false
    }

    private fun showNoTransactionsView() {
        txes_list.visible = false
        empty_view_layout.visible = true
    }

    private fun showPullToRefreshLoading() {
        pullToRefresh.isRefreshing = true
    }

    private fun stopPullToRefreshLoading() {
        pullToRefresh.isRefreshing = false
    }

    private fun showPullToRefreshLoadingError() {
        activity?.toast(R.string.loading_error)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home)
            activity?.onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun newInstance(accountEntityId: Long?): MainFragment {
            val fragment = MainFragment()
            fragment.accountEntityId = accountEntityId
            return fragment
        }
    }
}