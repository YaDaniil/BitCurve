package com.yadaniil.bitcurve.utils

import java.math.BigDecimal

/**
 * Created by danielyakovlev on 1/30/18.
 */

enum class Denomination constructor(private val factor: Long, val denominationName: String) {
    BTC(1, "BTC"),
    mBTC(1000, "mBTC"),
    uBTC(1000000, "uBTC"),
    SATOSHI(100000000, "Satoshi");

    val scale: Int
        get() = Math.log10((SATOSHI.factor / this.factor).toDouble()).toInt()

    fun fromBtc(value: BigDecimal): BigDecimal {
        return value.multiply(BigDecimal.valueOf(factor))
    }

    fun toBtc(value: BigDecimal): BigDecimal {
        return value.divide(BigDecimal.valueOf(factor))
    }
}