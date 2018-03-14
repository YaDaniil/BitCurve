package com.yadaniil.bitcurve.screens.account.send

import com.subgraph.orchid.encoders.Hex
import com.yadaniil.bitcurve.BaseViewModel
import com.yadaniil.bitcurve.data.Repository
import com.yadaniil.bitcurve.logic.AccountsManager
import com.yadaniil.bitcurve.logic.WalletHelper
import com.yadaniil.bitcurve.logic.send.TooSmallFeeException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.bitcoinj.core.Address
import org.bitcoinj.core.Coin
import org.bitcoinj.wallet.SendRequest
import timber.log.Timber

/**
 * Created by danielyakovlev on 3/13/18.
 */

class SendViewModel(private val walletHelper: WalletHelper,
                    private val repo: Repository,
                    private val accountsManager: AccountsManager) : BaseViewModel() {

    fun sendTx(address: String, btcValue: String) {
        val destinationAddress = Address.fromBase58(WalletHelper.params, address)
        val amountToSend = Coin.parseCoin(btcValue)
        val sendRequest = SendRequest.to(destinationAddress, amountToSend)
        sendRequest.feePerKb = Coin.parseCoin("0.01")
        sendRequest.changeAddress = accountsManager.getAccountById(0)?.changeAddresses?.first()
        val wallet = walletHelper.getWallet()
        wallet?.completeTx(sendRequest)
        wallet?.commitTx(sendRequest.tx)

        val hex = String(Hex.encode(sendRequest.tx.unsafeBitcoinSerialize()))
        repo.pushTx(hex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ responseString ->
                    Timber.e("Push tx response: $responseString")
                }, {
                    val error = it.message ?: ""
                    Timber.e("Push tx error: $error")
                    
                    if (error.contains("Very Small")) {
                        Timber.e("Push tx error: TooSmallFeeException")
                        throw TooSmallFeeException("Too small fee")
                    } else if (error.contains("500")) {

                    }
                })
    }
}