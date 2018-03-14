package com.yadaniil.bitcurve.data.api


/**
 * Created by danielyakovlev on 1/30/18.
 */

class AppApiHelper(private val blockchainInfoService: BlockchainInfoService) : BlockchainInfoService {

    override fun downloadInfoForAddresses(addressesString: String, n: Int?, offset: Int?)
            = blockchainInfoService.downloadInfoForAddresses(addressesString, n, offset)

    override fun downloadBlockHeight() = blockchainInfoService.downloadBlockHeight()

    override fun downloadUtxosForAddresses(addressesString: String, limit: Int?, confirmations: Int?)
            = blockchainInfoService.downloadUtxosForAddresses(addressesString, limit, confirmations)

    override fun pushTx(txHex: String) = blockchainInfoService.pushTx(txHex)
}