package com.yadaniil.bitcurve.screens.account.receive

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.yadaniil.bitcurve.BaseViewModel
import com.yadaniil.bitcurve.logic.WalletHelper
import org.bitcoinj.core.Address

/**
 * Created by danielyakovlev on 2/24/18.
 */

class ReceiveViewModel (private val walletHelper: WalletHelper) : BaseViewModel() {

    private lateinit var currentReceiveAddress: MutableLiveData<Address>

    fun observeCurrentReceiveAddress(): LiveData<Address> {
        currentReceiveAddress = MutableLiveData()
        currentReceiveAddress.postValue(walletHelper.getWallet()?.currentReceiveAddress())
        return currentReceiveAddress
    }

    fun freshReceiveAddress() {
        currentReceiveAddress.postValue(walletHelper.getWallet()?.freshReceiveAddress())
    }
}