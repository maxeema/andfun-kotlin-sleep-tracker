package com.example.android.trackmysleepquality.misc

import androidx.core.text.HtmlCompat

object Utils {

    fun fromHtml(s: String) = HtmlCompat.fromHtml(s, HtmlCompat.FROM_HTML_MODE_COMPACT)

    fun formatError(msgId: Int, err: Throwable) = fromHtml(
        app.getString(msgId).plus("<br/><br/><small>[ ${err.javaClass.simpleName} ]").let {
            if (err.message.isNullOrBlank()) it else it.plus("<br/>${err.message}")
        }.plus("</small>"))
}

