package com.yadaniil.bitcurve.utils

import android.view.MenuItem
import android.view.View

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