package com.example.android.trackmysleepquality.misc

import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.view.View
import android.view.animation.AnimationUtils

object Anim {

    private val fadeIn by lazy { AnimationUtils.loadAnimation(app, android.R.anim.fade_in) }

    fun fadeInOn(v: View) = with (fadeIn) {
        v.animation = this; start(); v.requestLayout()
    }

    fun startOn(d: Drawable?) { if (d is Animatable) d.start() }
    fun stopOn(d: Drawable?) { if (d is Animatable) d.stop() }

}
