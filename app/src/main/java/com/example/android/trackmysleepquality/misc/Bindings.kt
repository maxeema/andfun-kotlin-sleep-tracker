package com.example.android.trackmysleepquality.misc

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.android.trackmysleepquality.data.Night
import com.example.android.trackmysleepquality.ext.fromHtml
import com.example.android.trackmysleepquality.ext.logPeriod

@BindingAdapter("nightQuality")
fun bindNightQuality(view: TextView, night: Night?) = night?.apply {
    view.setText(quality.label)
}

@BindingAdapter("nightImage")
fun bindNightImage(view: ImageView, night: Night?) = night?.apply {
    bindNightQualityImage(view, quality)
}

@BindingAdapter("nightQualityImage") @SuppressLint("ResourceType")
fun bindNightQualityImage(view: ImageView, quality: Night.Quality?) = quality?.apply {
    Glide.with(view.context)
        .load(quality.img)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(view)
}

@BindingAdapter("nightPeriod")
fun bindNightPeriod(view: TextView, night: Night?) = night?.apply {
    view.text = night.logPeriod()
}

@BindingAdapter("textHtml")
fun TextView.setTextHtml(str: String) {
    text = str.fromHtml()
}
