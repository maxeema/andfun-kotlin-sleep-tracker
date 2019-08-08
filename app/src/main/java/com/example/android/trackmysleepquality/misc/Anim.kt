package com.example.android.trackmysleepquality.util

import android.view.View
import android.view.animation.AnimationUtils
import com.example.android.trackmysleepquality.app

object Anim {

    val fadeIn by lazy { AnimationUtils.loadAnimation(app, android.R.anim.fade_in) }

    fun fadeInOn(v: View) = with (fadeIn) {
        v.animation = this
        start()
        v.requestLayout()
    }

}