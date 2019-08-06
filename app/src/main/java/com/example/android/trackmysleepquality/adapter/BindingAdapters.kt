package com.example.android.trackmysleepquality.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.android.trackmysleepquality.data.Night
import com.example.android.trackmysleepquality.util.describe
import com.example.android.trackmysleepquality.util.log
import com.example.android.trackmysleepquality.util.present

@BindingAdapter("isGone")
fun bindIsGone(view: View, value: Boolean) { view.visibility = if (value) View.GONE else View.VISIBLE }
@BindingAdapter("isInvisible")
fun bindIsVisible(view: View, value: Boolean) { view.visibility = if (value) View.INVISIBLE else View.VISIBLE }

@BindingAdapter("nightQuality")
fun bindNightQuality(view: TextView, night: Night?) = night?.apply {
    view.text = night.describe()
}

@BindingAdapter("nightPeriod")
fun bindNightPeriod(view: TextView, night: Night?) = night?.apply {
    view.text = night.log()
}

@BindingAdapter("nightImage")
fun bindNightImage(view: ImageView, night: Night?) = night?.apply {
    Glide.with(view.context)
        .load(night.present())
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(view)
}
