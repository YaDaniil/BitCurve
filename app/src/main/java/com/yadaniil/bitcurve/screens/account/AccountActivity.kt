package com.yadaniil.bitcurve.screens.account

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.yadaniil.bitcurve.R
import com.yadaniil.bitcurve.screens.account.main.MainFragment
import com.yadaniil.bitcurve.screens.account.receive.ReceiveFragment
import com.yadaniil.bitcurve.screens.account.send.SendFragment
import com.yadaniil.bitcurve.utils.Navigator
import kotlinx.android.synthetic.main.activity_account.*

class AccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        initBottomNavigation()
        Navigator.openFragment(MainFragment.newInstance(), this, R.id.contentContainer)
    }

    private fun initBottomNavigation() {
        bottom_navigation.selectedItemId = R.id.action_account
        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_receive -> {
                    Navigator.openFragment(ReceiveFragment.newInstance(), this, R.id.contentContainer)
                    true
                }
                R.id.action_account -> {
                    Navigator.openFragment(MainFragment.newInstance(), this, R.id.contentContainer)
                    true
                }
                R.id.action_send -> {
                    Navigator.openFragment(SendFragment.newInstance(), this, R.id.contentContainer)
                    true
                }
                else -> true
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings ->
                return true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
