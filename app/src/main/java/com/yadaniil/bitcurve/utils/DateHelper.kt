package com.yadaniil.bitcurve.utils

import android.content.Context
import com.yadaniil.bitcurve.R
import com.yadaniil.bitcurve.data.db.models.TxEntity
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by danielyakovlev on 2/20/18.
 */

object DateHelper {

    private val MINUTE = 60
    private val HOUR = MINUTE * 60
    private val DAY = HOUR * 24

    fun buildTxFriendlyDateString(transaction: TxEntity, context: Context): String {

        val nowCalendar = Calendar.getInstance()
        val txDate = transaction.time
        val txCalendar = Calendar.getInstance()
        txCalendar.time = Date(txDate)

        val txDay = txCalendar.get(Calendar.DAY_OF_YEAR)
        val nowDay = nowCalendar.get(Calendar.DAY_OF_YEAR)

        val txUnix = transaction.time
        val nowUnix = Date().time / 1000
        val secondsDelta = nowUnix - txUnix

        val resources = context.resources

        if (secondsDelta < MINUTE) {
            return String.format("%s %s",
                    resources.getQuantityString(R.plurals.seconds, secondsDelta.toInt(), secondsDelta.toInt()),
                    resources.getString(R.string.ago))
        } else if (secondsDelta in MINUTE..(HOUR - 1)) {
            val minutesDelta = secondsDelta.toInt() / MINUTE
            return String.format("%s %s",
                    resources.getQuantityString(R.plurals.minutes, minutesDelta, minutesDelta),
                    resources.getString(R.string.ago))
        } else if (secondsDelta in HOUR..(DAY - 1)) {
            val hoursDelta = secondsDelta.toInt() / HOUR
            return String.format("%s %s",
                    resources.getQuantityString(R.plurals.hours, hoursDelta, hoursDelta),
                    resources.getString(R.string.ago))
        } else return if (nowCalendar.get(Calendar.YEAR) == txCalendar.get(Calendar.YEAR) && nowDay == txDay + 1) {
            resources.getString(R.string.yesterday)
        } else if (nowCalendar.get(Calendar.YEAR) == txCalendar.get(Calendar.YEAR)) {
            buildTxDayMonthString(transaction, false)
        } else {
            buildTxDayMonthString(transaction, true)
        }
    }

    private fun buildTxDayMonthString(transaction: TxEntity, withYear: Boolean): String {
        val date = Date(transaction.time)
        return if (Locale.getDefault().language.startsWith("en")) {
            SimpleDateFormat("MMMM d" + if (withYear) ", yyyy" else "").format(date)
        } else {
            SimpleDateFormat("d MMMM" + if (withYear) ", yyyy" else "").format(date)
        }
    }
}