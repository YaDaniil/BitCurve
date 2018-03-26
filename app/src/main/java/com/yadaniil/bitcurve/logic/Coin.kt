package com.yadaniil.bitcurve.logic

import com.yadaniil.bitcurve.R

/**
 * Created by danielyakovlev on 3/26/18.
 */

sealed class Coin(val name: String, val ticker: String, val coinPath: Int, val icon: Int)

class Bitcoin : Coin("Bitcoin", "BTC", 0, R.drawable.ic_btc)
class BitcoinCash : Coin("Bitcoin Cash", "BCH", 145, R.drawable.ic_bch_green)
class Litecoin : Coin("Litecoin", "LTC", 2, R.drawable.ic_ltc)