package com.yadaniil.bitcurve.screens.account.send

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.yadaniil.bitcurve.R
import kotlinx.android.synthetic.main.fragment_send.*

/**
 * Created by danielyakovlev on 2/24/18.
 */

class SendFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
            = inflater.inflate(R.layout.fragment_send, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        initToolbar()
    }

    private fun initToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp)
        toolbar.title = getString(R.string.send)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home)
            activity?.onBackPressed()
        return super.onOptionsItemSelected(item)
    }


    companion object {
        fun newInstance(): SendFragment {
            return SendFragment()
        }
    }
}