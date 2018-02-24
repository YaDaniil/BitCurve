package com.yadaniil.bitcurve.utils

import org.bitcoinj.core.Coin
import java.math.BigDecimal
import java.util.*

/**
 * Created by danielyakovlev on 1/30/18.
 */

object DenominationHelper {
    val availableDenominations: List<Denomination>
        get() = Arrays.asList(
                Denomination.BTC,
                Denomination.mBTC,
                Denomination.uBTC,
                Denomination.SATOSHI)


    fun btcToSatoshi(value: BigDecimal): Long {
        return Denomination.SATOSHI.fromBtc(value).longValueExact()
    }

    fun satoshiToBtc(amount: Long): BigDecimal {
        return Denomination.SATOSHI.toBtc(BigDecimal(amount))
    }

    fun satoshiToBtc(amount: Coin): BigDecimal {
        return satoshiToBtc(amount.value)
    }
}