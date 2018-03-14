package com.yadaniil.bitcurve.screens.settings

import android.arch.lifecycle.LifecycleObserver
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.yadaniil.bitcurve.R
import com.yadaniil.bitcurve.logic.WalletHelper
import com.yadaniil.bitcurve.logic.getMnemonic
import com.yadaniil.bitcurve.utils.Navigator
import org.jetbrains.anko.alert


/**
 * Created by danielyakovlev on 2/26/18.
 */


class SettingsActivity : AppCompatPreferenceActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // load settings fragment
        fragmentManager.beginTransaction().replace(android.R.id.content,
                MainPreferenceFragment()).commit()
    }

    class MainPreferenceFragment : PreferenceFragment() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.pref_settings)

            val myPref = findPreference(getString(R.string.key_send_feedback))
            myPref.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                sendFeedback(activity)
                true
            }

            val showMnemonicPreference = findPreference(getString(R.string.key_show_mnemonic))
            showMnemonicPreference.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                showMnemonic()
                true
            }

            val restoreWalletPreference = findPreference(getString(R.string.key_restore_wallet))
            restoreWalletPreference.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                Navigator.toMnemonicRestoreActivity(activity)
                true
            }
        }

        private fun showMnemonic() {
            val mnemonic = WalletHelper.wallet?.getMnemonic() ?: "Error"
            alert(mnemonic, getString(R.string.wallet_mnemonic_phrase_bip_44)) {
                positiveButton(R.string.copy) {
                    val clipboardManager = activity.getSystemService(AppCompatActivity.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("mnemonic", mnemonic)
                    clipboardManager.primaryClip = clip
                }
            }.show()
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        fun sendFeedback(context: Context) {
            var body: String? = null
            try {
                body = context.packageManager.getPackageInfo(context.packageName, 0).versionName
                body = "\n\n-----------------------------\nPlease don't remove this information\n Device OS: Android \n Device OS version: " +
                        Build.VERSION.RELEASE + "\n App Version: " + body + "\n Device Brand: " + Build.BRAND +
                        "\n Device Model: " + Build.MODEL + "\n Device Manufacturer: " + Build.MANUFACTURER
            } catch (e: PackageManager.NameNotFoundException) {
            }

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "message/rfc822"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("contact@androidhive.info"))
            intent.putExtra(Intent.EXTRA_SUBJECT, "Query from android app")
            intent.putExtra(Intent.EXTRA_TEXT, body)
            context.startActivity(Intent.createChooser(intent, context.getString(R.string.choose_email_client)))
        }
    }
}