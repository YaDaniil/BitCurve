package com.yadaniil.bitcurve.screens.account.receive

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yadaniil.bitcurve.R
import kotlinx.android.synthetic.main.fragment_receive.*

/**
 * Created by danielyakovlev on 2/24/18.
 */

class ReceiveFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
            = inflater.inflate(R.layout.fragment_receive, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.title = getString(R.string.receive)
    }


    companion object {
        fun newInstance(): ReceiveFragment {
            return ReceiveFragment()
        }
    }
}