package com.yadaniil.bitcurve.logic

import com.yadaniil.bitcurve.data.db.models.AccountEntity
import org.bitcoinj.core.Address
import org.bitcoinj.wallet.DeterministicKeyChain

/**
 * Created by danielyakovlev on 2/5/18.
 */

class Account(var accountEntity: AccountEntity,
              var keyChain: DeterministicKeyChain,
              var receiveAddresses: List<Address> = ArrayList(),
              var changeAddresses: List<Address> = ArrayList()) {

    fun getCurrentReceiveAddressForAccount(): Address {
        val currentReceiveAddressIndex =
                if (accountEntity.lastUsedReceiveAddressIndex == 0)
                    0
                else
                    accountEntity.lastUsedReceiveAddressIndex + 1


        return receiveAddresses[currentReceiveAddressIndex]
    }

    fun haveAddress(firstReceiver: String): Boolean {
        val receiveAddressesString: MutableList<String> = ArrayList()
        receiveAddresses.mapTo(receiveAddressesString) { it.toBase58() }
        return receiveAddressesString.contains(firstReceiver)
    }

    fun stringifyAddresses(): String {
        val addresses: MutableList<Address> = java.util.ArrayList()
        addresses.addAll(receiveAddresses)
        addresses.addAll(changeAddresses)
        val accountAddressesStrings = addresses.map { it.toBase58() }

        val result = StringBuilder()

        for (i in accountAddressesStrings.indices) {
            result.append(String.format("%s%s", if (i == 0) "" else "%7C", addresses[i]))
        }

        return result.toString()
    }
}