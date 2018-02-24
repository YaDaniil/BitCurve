package com.yadaniil.bitcurve.screens.splash

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.yadaniil.bitcurve.R
import com.yadaniil.bitcurve.screens.account.AccountActivity
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.koin.android.architecture.ext.viewModel

/**
 * Created by danielyakovlev on 2/24/18.
 */

class SplashActivity : AppCompatActivity() {

    private val viewModel by viewModel<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        initWalletLoadingObserver()
    }

    private fun initWalletLoadingObserver() {
        viewModel.loadWallet()
        viewModel.getIsWalletLoaded()?.observe(this,
                Observer { isWalletLoaded ->
                    if(isWalletLoaded == null)
                        return@Observer

                    if (isWalletLoaded) {
                        startActivity<AccountActivity>()
                        finish()
                    } else {
                        toast("Error")
                    }
                })
    }
}