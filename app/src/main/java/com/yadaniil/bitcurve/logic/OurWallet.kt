package com.yadaniil.bitcurve.logic

import com.google.common.base.Joiner
import org.bitcoinj.wallet.Wallet


/**
 * Created by danielyakovlev on 1/12/18.
 */
object OurWallet {

    var btcWallet: Wallet? = null

    fun getMnemonic(): String {
        val seed = btcWallet?.keyChainSeed
        return Joiner.on(' ').join(seed?.mnemonicCode!!)
    }

}