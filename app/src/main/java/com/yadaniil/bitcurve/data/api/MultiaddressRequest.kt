package com.yadaniil.bitcurve.data.api

import com.yadaniil.bitcurve.data.api.models.MultiaddressResponse
import org.bitcoinj.core.Address
import retrofit2.Call
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

/**
 * Created by danielyakovlev on 1/30/18.
 */

abstract class MultiaddressRequest {

    protected val TRANSACTIONS_PER_PAGE = 50
    protected var totalItems: Int = 0
    protected var lastResponseSize = TRANSACTIONS_PER_PAGE
    protected var addresses: String? = null
    protected var offset: Int = 0

    internal fun hasMorePages(): Boolean {
        return lastResponseSize == TRANSACTIONS_PER_PAGE && offset < totalItems
    }

    protected abstract fun incrementPage()

    protected abstract fun downloadNextPage(): Response<MultiaddressResponse>?


}