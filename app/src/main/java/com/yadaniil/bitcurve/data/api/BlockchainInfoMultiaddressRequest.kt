package com.yadaniil.bitcurve.data.api

import com.yadaniil.bitcurve.data.api.models.MultiaddressResponse
import org.bitcoinj.core.Address
import retrofit2.Response

/**
 * Created by danielyakovlev on 11/25/17.
 */

class BlockchainInfoMultiaddressRequest(private val addressesList: MutableList<Address>) : MultiaddressRequest() {

    override fun incrementPage() {
        offset += TRANSACTIONS_PER_PAGE
    }

    override fun downloadNextPage(): Response<MultiaddressResponse>? {
//        val rawResponse = ApiFactory.getNodeService()
//                .transactionsBlockchainInfo(stringify(addressesList), TRANSACTIONS_PER_PAGE, offset)
//        val response = handleResponse(rawResponse)
//
//        if (response != null && response.isSuccessful) {
//            lastResponseSize = response.body().txs.size
//            totalItems = offset + response.body().txs.size
//            incrementPage()
//        }

//        return response
        return null
    }

}