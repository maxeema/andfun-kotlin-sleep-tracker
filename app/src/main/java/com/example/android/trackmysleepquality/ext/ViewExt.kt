package com.example.android.trackmysleepquality.ext

import android.app.Activity
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
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
fun <T : ViewGroup.LayoutParams> T.constraint() = this as ConstraintLayout.LayoutParams
