package com.yadaniil.bitcurve.screens.tx

import com.yadaniil.bitcurve.BaseViewModel
import com.yadaniil.bitcurve.data.Repository

/**
 * Created by danielyakovlev on 3/27/18.
 */

class TxViewModel(private val repo: Repository) : BaseViewModel() {

    fun getTx(txEntityId: Long) = repo.getTx(txEntityId)

    fun getBlockchainHeight() = repo.getBlockHeight()

    fun getAccountOfTx(txEntityId: Long) =
            repo.getAccount(repo.getAccountEntityIdOfTx(txEntityId))

}