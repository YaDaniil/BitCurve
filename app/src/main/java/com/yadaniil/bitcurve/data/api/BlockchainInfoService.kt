package com.yadaniil.bitcurve.data.api

import com.yadaniil.bitcurve.data.api.models.MultiaddressResponse
import com.yadaniil.bitcurve.data.api.models.UtxosResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by danielyakovlev on 1/30/18.
 */

interface BlockchainInfoService {

    @GET("multiaddr")
    fun downloadInfoForAddresses(
            @Query("active", encoded = true) addressesString: String,
            @Query("n") n: Int? = null,
            @Query("offset") offset: Int? = null): Observable<MultiaddressResponse>

    @GET("q/getblockcount")
    fun downloadBlockHeight(): Observable<Int>

    @GET("unspent")
    fun downloadUtxosForAddresses(
            @Query("active", encoded = true) addressesString: String,
            @Query("limit") limit: Int? = null,
            @Query("confirmations") confirmations: Int? = null): Observable<UtxosResponse>
}