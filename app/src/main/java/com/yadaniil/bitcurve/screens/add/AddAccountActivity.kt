package com.yadaniil.bitcurve.screens.add

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import com.yadaniil.bitcurve.R
import kotlinx.android.synthetic.main.coin_picker_bch_item_layout.*
import kotlinx.android.synthetic.main.coin_picker_bottom_sheet.*
import kotlinx.android.synthetic.main.coin_picker_btc_item_layout.*
import kotlinx.android.synthetic.main.coin_picker_ltc_item_layout.*
import kotlinx.android.synthetic.main.item_currency.*
import org.jetbrains.anko.find
import org.jetbrains.anko.onClick
import org.koin.android.architecture.ext.viewModel

/**
 * Created by danielyakovlev on 3/16/18.
 */

class AddAccountActivity : AppCompatActivity() {

    private val viewModel by viewModel<AddAccountViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_account)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initCurrencyPicker()
    }

    private fun initCurrencyPicker() {
        currency_icon.setImageResource(R.drawable.ic_btc_png)
        currency_symbol.text = "BTC"
        currency_name.text = "Bitcoin"

        val bottomSheetBehavior = BottomSheetBehavior.from(coin_picker_bottom_sheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        item_currency_layout.onClick {
            if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
            } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN)
            }
        }


        picker_btc.onClick {
            currency_icon.setImageResource(R.drawable.ic_btc_png)
            currency_symbol.text = "BTC"
            currency_name.text = "Bitcoin"
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN)
        }

        picker_bch.onClick {
            currency_icon.setImageResource(R.drawable.ic_bch_png)
            currency_symbol.text = "BCH"
            currency_name.text = "Bitcoin Cash"
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN)
        }

        picker_ltc.onClick {
            currency_icon.setImageResource(R.drawable.ic_ltc)
            currency_symbol.text = "LTC"
            currency_name.text = "Litecoin"
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_check, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.action_done -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}