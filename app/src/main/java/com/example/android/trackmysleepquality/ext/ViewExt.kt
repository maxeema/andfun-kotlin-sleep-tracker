package com.example.android.trackmysleepquality.ext

import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.misc.Anim

/**
 * Views extensions
 */

fun View.animFadeIn() = Anim.fadeInOn(this)

fun Drawable?.startIfItAnimatable() { Anim.startOn(this) }
fun Drawable?.stopIfItAnimatable() { Anim.stopOn(this) }

fun <T : RecyclerView.LayoutManager> T?.grid() = this as GridLayoutManager
