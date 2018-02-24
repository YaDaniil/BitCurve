package com.yadaniil.bitcurve.utils

import com.yadaniil.bitcurve.logic.Account
import com.yadaniil.bitcurve.logic.OurWallet
import org.bitcoinj.core.AddressFormatException
import org.bitcoinj.core.Base58
import org.bitcoinj.crypto.DeterministicKey
import org.bitcoinj.crypto.HDKeyDerivation
import org.bitcoinj.wallet.DeterministicKeyChain
import java.nio.ByteBuffer

/**
 * Created by danielyakovlev on 1/17/18.
 */
object XPubHelper {

    fun isXpubBip32(xpubString: String): Boolean {
        val xpubBytes = Base58.decodeChecked(xpubString)
        val bb = ByteBuffer.wrap(xpubBytes)
        bb.int
        // depth:
        val depth = bb.get()
        return depth.toInt() == 1
    }

    fun isXpubBip44(xpubString: String): Boolean {
        val xpubBytes = Base58.decodeChecked(xpubString)
        val bb = ByteBuffer.wrap(xpubBytes)
        bb.int
        // depth:
        val depth = bb.get()
        return depth.toInt() == 3
    }

    fun getXpubForAccount(keychain: DeterministicKeyChain): String {
        return keychain.watchingKey.serializePubB58(OurWallet.btcWallet?.params)
    }

    fun getXpubForAccount(account: Account): String {
        return account.keyChain.watchingKey.serializePubB58(OurWallet.btcWallet?.params)
    }

    @Throws(AddressFormatException::class)
    fun createMasterPubKeyFromXPub(xpubstr: String): DeterministicKey {

        val xpubBytes = Base58.decodeChecked(xpubstr)

        val bb = ByteBuffer.wrap(xpubBytes)
        val magic = bb.int
        if (magic != 0x0488B21E && magic != 0x049D7CB2) {
            throw AddressFormatException("invalid xpub version")
        }

        val chain = ByteArray(32)
        val pub = ByteArray(33)
        // depth:
        bb.get()
        // parent fingerprint:
        bb.int
        // child no.
        bb.int
        bb.get(chain)
        bb.get(pub)

        return HDKeyDerivation.createMasterPubKeyFromBytes(pub, chain)
    }
}