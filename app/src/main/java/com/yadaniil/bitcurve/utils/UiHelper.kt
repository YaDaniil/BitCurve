package com.yadaniil.bitcurve.utils

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Created by danielyakovlev on 1/15/18.
 */

fun MenuItem.enable() {
    this.isEnabled = true
    this.icon.mutate().alpha = 255
}

fun MenuItem.disable() {
    this.isEnabled = false
    this.icon.mutate().alpha = 130
}

var View.visible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if(value) View.VISIBLE else View.GONE
    }

fun hideKeyboard(activity: AppCompatActivity) {
    val view = activity.currentFocus
    if (view != null) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

