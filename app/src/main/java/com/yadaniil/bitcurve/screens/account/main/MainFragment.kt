package com.yadaniil.bitcurve.screens.account.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yadaniil.bitcurve.R
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * Created by danielyakovlev on 2/24/18.
 */

class MainFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
            = inflater.inflate(R.layout.fragment_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_layout.title = "MainFragment"

    }


    companion object {
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }
}