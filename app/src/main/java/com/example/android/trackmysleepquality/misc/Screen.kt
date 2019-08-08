package com.example.android.trackmysleepquality.misc

import android.os.Handler
import android.view.View
import android.view.Window
import androidx.lifecycle.Lifecycle

class Screen(private val sleepTimeout: Long, private val window: Window?, private val lifecycle: Lifecycle) {

    private val h by lazy { Handler() }
    private val flags = window?.decorView?.systemUiVisibility

    fun sleep() {
        window?.decorView?.apply {
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED))
                systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE
        }
    }
    fun awake(fully: Boolean = false) {
        window?.decorView?.apply {
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                systemUiVisibility = flags!!
                h.removeCallbacksAndMessages(null)
                if (!fully) h.postDelayed(::sleep, sleepTimeout)
            }
        }
    }

}
