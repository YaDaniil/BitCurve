package com.yadaniil.bitcurve.utils

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import com.yadaniil.bitcurve.R
import com.yadaniil.bitcurve.screens.ScanQrActivity

/**
 * Created by danielyakovlev on 1/15/18.
 */
object Navigator {

    fun openFragment(fragment: Fragment, activity: AppCompatActivity, container: Int) {
        val ft = activity.supportFragmentManager.beginTransaction()
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
        val fragmentByTag = activity.supportFragmentManager.findFragmentByTag(fragment.javaClass.simpleName)
        if (fragmentByTag != null) {
            ft.replace(container, fragmentByTag, fragmentByTag.javaClass.simpleName)
        } else {
            ft.replace(container, fragment, fragment.javaClass.simpleName)
        }
        ft.commit()
    }

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