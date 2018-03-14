package com.yadaniil.bitcurve.screens.account.send

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.google.zxing.integration.android.IntentIntegrator
import com.yadaniil.bitcurve.R
import com.yadaniil.bitcurve.utils.Navigator
import com.yadaniil.bitcurve.utils.hideKeyboard
import com.yadaniil.bitcurve.utils.visible
import kotlinx.android.synthetic.main.fragment_send.*
import org.jetbrains.anko.onClick
import org.jetbrains.anko.toast
import org.koin.android.architecture.ext.viewModel
import timber.log.Timber

/**
 * Created by danielyakovlev on 2/24/18.
 */

class SendFragment : Fragment() {

    private val viewModel by viewModel<SendViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_send, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        initToolbar()
        initFeeSegmentedControl()
        scan_qr_image_button.onClick { Navigator.openQrScanner(this) }
    }

    private fun initToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp)
        toolbar.title = getString(R.string.send)
    }

    private fun initFeeSegmentedControl() {
        fee_segmented_control.check(R.id.normal_button)
        fee_segmented_control.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.low_button -> custom_fee_slider.visible = false
                R.id.normal_button -> custom_fee_slider.visible = false
                R.id.high_button -> custom_fee_slider.visible = false
                R.id.custom_button -> showCustomFeeSlider()
            }
        }
    }

    private fun showCustomFeeSlider() {
        hideKeyboard(activity as AppCompatActivity)
        custom_fee_slider.visible = true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent)
        Timber.e(result.toString())
        if (result != null) {
            if (result.contents != null)
                address_edit_text.setText(result.contents.toString())
        } else {
            super.onActivityResult(requestCode, resultCode, intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        activity?.menuInflater?.inflate(R.menu.menu_send, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home)
            activity?.onBackPressed()
        else if (item?.itemId == R.id.action_send) {
            activity?.toast(R.string.send)
            viewModel.sendTx(address_edit_text.text.toString(), amount_edit_text.text.toString())
        }

        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun newInstance(): SendFragment {
            return SendFragment()
        }
    }
}