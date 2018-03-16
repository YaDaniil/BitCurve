package com.yadaniil.bitcurve.screens.accounts

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.yadaniil.bitcurve.R
import com.yadaniil.bitcurve.data.db.models.AccountEntity
import com.yadaniil.bitcurve.screens.add.AddAccountActivity
import com.yadaniil.bitcurve.utils.Navigator
import com.yadaniil.bitcurve.utils.visible
import kotlinx.android.synthetic.main.activity_accounts.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.koin.android.architecture.ext.viewModel

/**
 * Created by danielyakovlev on 3/12/18.
 */


class AccountsActivity : AppCompatActivity(), AccountsAdapter.OnAccountClick {


    private val viewModel by viewModel<AccountsViewModel>()
    private lateinit var accountsAdapter: AccountsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accounts)
        initMnemonicWarning()
        initAccountsList()
        initPullToRefresh()
        initAddNewAccountFab()
    }

    // region Init
    private fun initMnemonicWarning() {
        viewModel.getIsMnemonicWrittenDown().observe(this,
                Observer { isMnemonicWrittenDown ->
                    isMnemonicWrittenDown?.let {
                        if (it) hideMnemonicWarning() else showMnemonicWarning()
                    }
                })

        mnemonic_warning_layout.onClick {
            alert(R.string.save_mnemonic_description, R.string.save_mnemonic_title) {
                negativeButton(R.string.later)
                positiveButton(R.string.view) {
                    showMnemonicDialog()
                }
            }.show()
        }
    }

    private fun initAccountsList() {
        accountsAdapter = AccountsAdapter(this)
        list.layoutManager = LinearLayoutManager(this)
        list.setHasFixedSize(true)
        list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        list.adapter = accountsAdapter

        viewModel.getAccountLiveData().observe(this, Observer {
            it?.let {
                if (it.isNotEmpty())
                    accountsAdapter.setAccounts(it)
            }
        })
    }

    private fun initPullToRefresh() {
        pullToRefresh.setOnRefreshListener {
            viewModel.updateAll(accountsAdapter.getAccounts())
        }

        viewModel.getLoadingStateLiveData().observe(this, Observer {
            it?.let { isLoading ->
                if(isLoading) showPullToRefreshLoading() else stopPullToRefreshLoading()
            }
        })
    }

    private fun initAddNewAccountFab() {
        add_account_fab.onClick {
            startActivity<AddAccountActivity>()
        }
    }
    // endregion Init

    // region View
    private fun showMnemonicDialog() {
        alert(viewModel.getMnemonic(), getString(R.string.wallet_mnemonic_phrase_bip_44)) {
            positiveButton(R.string.mnemonic_phrase_is_written_down) {
                viewModel.setMnemonicWrittenDown(true)
                hideMnemonicWarning()
            }
        }.show()
    }

    override fun onClick(holder: AccountsAdapter.AccountHolder, item: AccountEntity) {
        Navigator.toAccountActivity(this, viewModel.getAccountById(item.accountId)!!)
    }

    private fun showMnemonicWarning() {
        mnemonic_warning_layout.visible = true
    }

    private fun hideMnemonicWarning() {
        mnemonic_warning_layout.visible = false
    }

    private fun showPullToRefreshLoading() {
        pullToRefresh.isRefreshing = true
    }

    private fun stopPullToRefreshLoading() {
        pullToRefresh.isRefreshing = false
    }

    private fun showPullToRefreshLoadingError() {
        toast(R.string.loading_error)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                Navigator.toSettingsActivity(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    // endregion View
}