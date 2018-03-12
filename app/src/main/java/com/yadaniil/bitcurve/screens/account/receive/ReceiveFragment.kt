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

/**
 * Created by danielyakovlev on 2/24/18.
 */

class ReceiveFragment : Fragment() {

    private val viewModel by viewModel<ReceiveViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
            = inflater.inflate(R.layout.fragment_receive, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.title = getString(R.string.receive)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        viewModel.observeCurrentReceiveAddress().observe(this, Observer {
            val currentReceiveAddressString = it?.toBase58()
            val myBitmap = QRCode.from(currentReceiveAddressString).withSize(400, 400).bitmap()
            qr_code.setImageBitmap(myBitmap)
            address.text = currentReceiveAddressString
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        activity?.menuInflater?.inflate(R.menu.menu_receive, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_fresh_receive_address -> {
                viewModel.freshReceiveAddress()
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    companion object {
        fun newInstance(): ReceiveFragment {
            return ReceiveFragment()
        }
    }
}