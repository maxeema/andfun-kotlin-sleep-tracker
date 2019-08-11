package com.example.android.trackmysleepquality.misc

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.android.trackmysleepquality.data.Night
import com.example.android.trackmysleepquality.ext.asString
import com.example.android.trackmysleepquality.ext.fromHtml
import com.example.android.trackmysleepquality.ext.logPeriod
import com.example.android.trackmysleepquality.ext.logPeriodEnd
import com.example.android.trackmysleepquality.misc.Utils.loadNightImage

@BindingAdapter("srcOfNight")
fun bindSrcOfNight(view: ImageView, night: Night?) = night?.apply {
    loadNightImage(view, quality)
}
@BindingAdapter("srcOfNightQuality")
fun bindSrcOfNightQuality(view: ImageView, quality: Night.Quality?) = quality?.apply {
    loadNightImage(view, quality)
}
@BindingAdapter("srcOfNightCached")
fun bindSrcOfNightCached(view: ImageView, night: Night?) = night?.apply {
    loadNightImage(view, quality, true)
}

@BindingAdapter("textOfNightPeriod")
fun bindTextOfNightPeriod(view: TextView, night: Night?) = night?.apply {
    view.text = logPeriod()
}
@BindingAdapter("textOfNightPeriodEnd")
fun bindTextOfNightPeriodEnd(view: TextView, night: Night?) = night?.apply {
    view.text = logPeriodEnd()
}
@BindingAdapter("textOfNightQuality")
fun bindTextOfNightQuality(view: TextView, night: Night?) = night?.apply {
    view.text = quality.label.asString()
}

@BindingAdapter("textHtml")
fun TextView.setTextHtml(str: String) {
    text = str.fromHtml()
}
