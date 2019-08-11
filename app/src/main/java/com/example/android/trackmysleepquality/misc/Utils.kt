package com.example.android.trackmysleepquality.misc

import android.annotation.SuppressLint
import android.widget.ImageView
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.data.Night
import com.example.android.trackmysleepquality.ext.asString
import com.example.android.trackmysleepquality.ext.fromHtml

object Utils {

    fun fromHtml(s: String) = HtmlCompat.fromHtml(s, HtmlCompat.FROM_HTML_MODE_COMPACT)

    fun formatError(msgId: Int, err: Throwable) =
        msgId.asString().plus("<br/><br/><small>[ ${err.javaClass.simpleName} ]").let {
            if (err.message.isNullOrBlank()) it else it.plus("<br/>${err.message}")
        }.plus("</small>").fromHtml()

    @SuppressLint("ResourceType")
    fun loadNightImage(view: ImageView, quality: Night.Quality, cached: Boolean = false) =
        Glide.with(view).load(quality.img).apply {
            if (!cached)
                skipMemoryCache(true)
    //        .transition(DrawableTransitionOptions.withCrossFade())
            placeholder(R.drawable.undefined)
            into(view)
        }

}

