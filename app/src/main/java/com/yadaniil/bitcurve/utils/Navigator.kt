package com.yadaniil.bitcurve.utils

import android.app.Activity
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import com.yadaniil.bitcurve.R
import com.yadaniil.bitcurve.logic.Account
import com.yadaniil.bitcurve.screens.ScanQrActivity
import com.yadaniil.bitcurve.screens.account.AccountActivity
import com.yadaniil.bitcurve.screens.accounts.AccountsActivity
import com.yadaniil.bitcurve.screens.settings.SettingsActivity
import com.yadaniil.bitcurve.screens.settings.restore.MnemonicRestoreActivity
import org.jetbrains.anko.*

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

    fun openQrScanner(fragment: Fragment) {
        IntentIntegrator.forSupportFragment(fragment)
                .setOrientationLocked(false)
                .setBeepEnabled(false)
                .setCaptureActivity(ScanQrActivity::class.java)
                .setPrompt(fragment.getString(R.string.scan_qr_description))
                .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
                .initiateScan()
    }

    fun toAccountActivity(activity: AppCompatActivity, account: Account) {
        activity.startActivity<AccountActivity>("accountEntityId" to account.accountEntity.id)
    }

    fun toMnemonicRestoreActivity(activity: Activity) {
        activity.startActivity<MnemonicRestoreActivity>()
    }

    fun toAccountsActivity(activity: AppCompatActivity) {
        activity.startActivity(activity.intentFor<AccountsActivity>().singleTop().clearTask().newTask())
    }

    fun toSettingsActivity(activity: Activity) {
        activity.startActivity<SettingsActivity>()
    }
}