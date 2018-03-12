package com.yadaniil.bitcurve.screens.account.receive

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.yadaniil.bitcurve.BaseViewModel
import com.yadaniil.bitcurve.logic.AccountsManager
import org.bitcoinj.core.Address

/**
 * Created by danielyakovlev on 2/24/18.
 */

class ReceiveViewModel (private val accountsManager: AccountsManager) : BaseViewModel() {

    private lateinit var currentReceiveAddress: MutableLiveData<Address>

    fun observeCurrentReceiveAddress(accountEntityId: Long): LiveData<Address> {
        currentReceiveAddress = MutableLiveData()

        currentReceiveAddress.postValue(accountsManager
                .getAccountByEntityId(accountEntityId)?.getCurrentReceiveAddressForAccount())
        return currentReceiveAddress
    }

    fun freshReceiveAddress(accountEntityId: Long) {
        currentReceiveAddress.postValue(accountsManager
                .getAccountByEntityId(accountEntityId)?.getCurrentReceiveAddressForAccount())
    }
}