package com.example.android.trackmysleepquality.misc

import androidx.core.text.HtmlCompat
import com.example.android.trackmysleepquality.ext.asString
import com.example.android.trackmysleepquality.ext.fromHtml

object Utils {

    fun fromHtml(s: String) = HtmlCompat.fromHtml(s, HtmlCompat.FROM_HTML_MODE_COMPACT)

    fun formatError(msgId: Int, err: Throwable) =
        msgId.asString().plus("<br/><br/><small>[ ${err.javaClass.simpleName} ]").let {
            if (err.message.isNullOrBlank()) it else it.plus("<br/>${err.message}")
        }.plus("</small>").fromHtml()

}

