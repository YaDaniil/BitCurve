package com.yadaniil.bitcurve.screens.account.receive

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.yadaniil.bitcurve.BaseViewModel
import com.yadaniil.bitcurve.logic.AccountsManager
import com.yadaniil.bitcurve.logic.FullGapLimitException
import org.bitcoinj.core.Address
import timber.log.Timber

/**
 * Created by danielyakovlev on 2/24/18.
 */

class ReceiveViewModel(private val accountsManager: AccountsManager) : BaseViewModel() {

    private lateinit var currentReceiveAddress: MutableLiveData<Address>
    private lateinit var currentGapLimit: MutableLiveData<Int>

    fun observeCurrentReceiveAddress(accountEntityId: Long): LiveData<Address> {
        currentReceiveAddress = MutableLiveData()

        currentReceiveAddress.postValue(accountsManager.getCurrentReceiveAddressOfAccount(accountEntityId))
        return currentReceiveAddress
    }

    fun observeGapLimit(accountEntityId: Long): LiveData<Int> {
        currentGapLimit = MutableLiveData()
        updateGapLimit(accountEntityId)
        return currentGapLimit
    }

    private fun updateGapLimit(accountEntityId: Long) {
        val accountEntity = accountsManager.getAccountByEntityId(accountEntityId)?.accountEntity
        val currentGap = accountEntity?.lastIssuedReceiveAddressIndex
                ?.minus(accountEntity.lastUsedReceiveAddressIndex)
        currentGapLimit.postValue(currentGap)
    }

    fun freshReceiveAddress(accountEntityId: Long) {
        try {
            currentReceiveAddress.postValue(accountsManager.getFreshReceiveAddressOfAccount(accountEntityId))
            updateGapLimit(accountEntityId)
        } catch (e: FullGapLimitException) {
            Timber.e("FullGapLimitException")
        }
    }
}