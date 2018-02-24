package com.yadaniil.bitcurve

import android.arch.lifecycle.LifecycleObserver
import io.objectbox.reactive.DataSubscription
import android.arch.lifecycle.ViewModel


/**
 * Created by danielyakovlev on 2/17/18.
 */

abstract class BaseViewModel : ViewModel(), LifecycleObserver {

    private val subscriptions: MutableList<DataSubscription>

    init {
        subscriptions = ArrayList()
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions
                .filterNot { it.isCanceled }
                .forEach { it.cancel() }
    }

    protected fun addSubscription(subscription: DataSubscription) =
            subscriptions.add(subscription)
}