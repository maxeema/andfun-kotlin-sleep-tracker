package com.example.android.trackmysleepquality.adapter

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.android.trackmysleepquality.data.Night
import com.example.android.trackmysleepquality.data.describe
import com.example.android.trackmysleepquality.data.log
import com.example.android.trackmysleepquality.data.present

@BindingAdapter("nightQuality")
fun bindNightQuality(view: TextView, night: Night?) = night?.apply {
    view.text = night.describe()
}

@BindingAdapter("nightTimeline")
fun bindNightTimeline(view: TextView, night: Night?) = night?.apply {
    view.text = night.log()
}

@BindingAdapter("nightImage")
fun bindNightImage(view: ImageView, night: Night?) = night?.apply {
    Glide.with(view.context)
        .load(night.present())
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(view)
}
