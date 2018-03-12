package com.yadaniil.bitcurve.screens.account.receive

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.yadaniil.bitcurve.R
import kotlinx.android.synthetic.main.fragment_receive.*
import net.glxn.qrgen.android.QRCode
import org.koin.android.architecture.ext.viewModel
import timber.log.Timber

/**
 * Created by danielyakovlev on 2/24/18.
 */

class ReceiveFragment : Fragment() {

    private val viewModel by viewModel<ReceiveViewModel>()
    private var accountEntityId: Long? = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
            = inflater.inflate(R.layout.fragment_receive, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        initToolbar()

        viewModel.observeCurrentReceiveAddress(accountEntityId ?: 0).observe(this, Observer {
            val currentReceiveAddressString = it?.toBase58()
            Timber.d("currentReceiveAddressString: $currentReceiveAddressString")
            val myBitmap = QRCode.from(currentReceiveAddressString).withSize(400, 400).bitmap()
            qr_code.setImageBitmap(myBitmap)
            address.text = currentReceiveAddressString
        })
    }

    private fun initToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp)
        toolbar.title = getString(R.string.receive)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        activity?.menuInflater?.inflate(R.menu.menu_receive, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_fresh_receive_address -> {
                viewModel.freshReceiveAddress(accountEntityId ?: 0)
                return true
            }
            android.R.id.home -> { activity?.onBackPressed(); true }

            else -> super.onOptionsItemSelected(item)
        }
    }


    companion object {
        fun newInstance(accountEntityId: Long?): ReceiveFragment {
            val fragment = ReceiveFragment()
            fragment.accountEntityId = accountEntityId
            return fragment
        }
    }
}