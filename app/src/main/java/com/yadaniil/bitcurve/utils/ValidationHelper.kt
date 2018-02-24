package com.yadaniil.bitcurve.utils

import com.yadaniil.bitcurve.Application
import org.bitcoinj.core.AddressFormatException
import org.bitcoinj.core.Base58
import java.nio.ByteBuffer

/**
 * Created by danielyakovlev on 1/13/18.
 */
object ValidationHelper {


    fun isValidXpub(xpub: String): Boolean {
        if(Application.isTestnet)
            return true

        val magicXpub = 0x0488B21E
        val magicYpub = 0x049D7CB2

        try {
            val xpubBytes = Base58.decodeChecked(xpub)

            val byteBuffer = ByteBuffer.wrap(xpubBytes)
            val magic = byteBuffer.int
            if (magic != magicXpub && magic != magicYpub) {
                throw AddressFormatException("invalid version: " + xpub)
            } else {

                val chain = ByteArray(32)
                val pub = ByteArray(33)
                // depth:
                byteBuffer.get()
                // parent fingerprint:
                byteBuffer.int
                // child no.
                byteBuffer.int
                byteBuffer.get(chain)
                byteBuffer.get(pub)

                val pubBytes = ByteBuffer.wrap(pub)
                val firstByte = pubBytes.get().toInt()
                return if (firstByte == 0x02 || firstByte == 0x03) {
                    true
                } else {
                    throw AddressFormatException("invalid format: " + xpub)
                }
            }
        } catch (e: Exception) {
            return false
        }

    }
}