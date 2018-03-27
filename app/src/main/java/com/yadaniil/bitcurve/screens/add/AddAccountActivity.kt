package com.yadaniil.bitcurve.screens.add

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.yadaniil.bitcurve.R
import com.yadaniil.bitcurve.logic.Bitcoin
import com.yadaniil.bitcurve.utils.Navigator
import com.yadaniil.bitcurve.utils.disable
import com.yadaniil.bitcurve.utils.enable
import com.yadaniil.bitcurve.utils.hideKeyboard
import kotlinx.android.synthetic.main.activity_add_account_fields.*
import kotlinx.android.synthetic.main.coin_picker_bch_item_layout.*
import kotlinx.android.synthetic.main.coin_picker_bottom_sheet.*
import kotlinx.android.synthetic.main.coin_picker_btc_item_layout.*
import kotlinx.android.synthetic.main.coin_picker_ltc_item_layout.*
import kotlinx.android.synthetic.main.item_currency.*
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.onClick
import org.jetbrains.anko.textChangedListener
import org.jetbrains.anko.toast
import org.koin.android.architecture.ext.viewModel

/**
 * Created by danielyakovlev on 3/16/18.
 */

class AddAccountActivity : AppCompatActivity() {

    private val viewModel by viewModel<AddAccountViewModel>()
    private lateinit var doneMenuItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_account)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initCurrencyPicker()
        account_name.textChangedListener {
            afterTextChanged {
                it?.let { if (it.isBlank()) doneMenuItem.disable() else doneMenuItem.enable() }
            }
        }
    }

    private fun initCurrencyPicker() {
        currency_icon.setImageResource(Bitcoin().icon)
        currency_symbol.text = Bitcoin().ticker
        currency_name.text = Bitcoin().name

        val bottomSheetBehavior = BottomSheetBehavior.from(coin_picker_bottom_sheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        item_currency_layout.onClick {
            hideKeyboard(this)
            if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
            } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN)
            }
        }

        picker_btc.onClick {
            currency_icon.setImageResource(Bitcoin().icon)
            currency_symbol.text = Bitcoin().ticker
            currency_name.text = Bitcoin().name
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN)
        }

        picker_bch.onClick {
            toast(R.string.bch_is_not_supported_yet)
//            currency_icon.setImageResource(BitcoinCash().icon)
//            currency_symbol.text = BitcoinCash().ticker
//            currency_name.text = BitcoinCash().name
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN)
        }

        picker_ltc.onClick {
            toast(R.string.ltc_is_not_supported_yet)
//            currency_icon.setImageResource(Litecoin().icon)
//            currency_symbol.text = Litecoin().ticker
//            currency_name.text = Litecoin().name
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_check, menu)
        doneMenuItem = menu?.findItem(R.id.action_done)!!
        doneMenuItem.disable()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.action_done -> {
                val progress = indeterminateProgressDialog(R.string.loading) { setCancelable(false) }
                progress.show()
                viewModel.createAccount(account_name.text.toString(), currency_name.text.toString())
                        .observe(this, Observer {
                            it?.let { isAccountCreated ->
                                progress.dismiss()
                                if (isAccountCreated)
                                    Navigator.toAccountsActivity(this)
                                else
                                    toast(R.string.wallet_restoring_error)
                            }
                        })
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}