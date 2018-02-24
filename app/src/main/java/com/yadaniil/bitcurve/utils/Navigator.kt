package com.yadaniil.bitcurve.utils

import android.support.v7.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import com.yadaniil.bitcurve.R
import com.yadaniil.bitcurve.ScanQrActivity

/**
 * Created by danielyakovlev on 1/15/18.
 */
object Navigator {

    fun openQrScanner(activity: AppCompatActivity) {
        IntentIntegrator(activity)
                .setOrientationLocked(false)
                .setBeepEnabled(false)
                .setCaptureActivity(ScanQrActivity::class.java)
                .setPrompt(activity.getString(R.string.scan_qr_description))
                .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
                .initiateScan()
    }
}