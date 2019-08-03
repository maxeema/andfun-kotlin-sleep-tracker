/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.core.text.HtmlCompat
import com.example.android.trackmysleepquality.database.SleepNight
import java.lang.ref.SoftReference
import java.text.SimpleDateFormat

/**
 * These functions create a formatted string that can be set in a TextView.
 */

private val qualityStr = arrayOf(
        R.string.zero_very_bad,   R.string.one_poor,           R.string.two_soso,
        R.string.three_ok,        R.string.four_pretty_good,   R.string.five_excellent)
fun convertNumericQualityToString(quality: Int)
        = if (quality in 0..5) app.getString(qualityStr[quality]) else  "- - -"

private val imsSleepCache = arrayOfNulls<SoftReference<Drawable>>(6)
private val imgSleepActive by lazy { app.getDrawable(R.drawable.ic_sleep_active)!! }
fun convertNumericQualityToImage(quality: Int) = when (quality) {
    in 0..5 -> imsSleepCache[quality]?.get()
        ?: app.getDrawable(app.resources.getIdentifier("ic_sleep_$quality", "drawable", app.packageName))!!.apply {
            imsSleepCache[quality] = SoftReference(this)
        }
    else -> imgSleepActive
}


/**
 * Take the Long milliseconds returned by the system and stored in Room,
 * and convert it to a nicely formatted string for display.
 *
 * EEEE - Display the long letter version of the weekday
 * MMM - Display the letter abbreviation of the nmotny
 * dd-yyyy - day in month and full year numerically
 * HH:mm - Hours and minutes in 24hr format
 */
@SuppressLint("SimpleDateFormat")
fun convertLongToDateString(systemTime: Long): String {
    return SimpleDateFormat("EEEE MMM-dd-yyyy' Time: 'HH:mm")
            .format(systemTime).toString()
}

/**
 * Takes a list of SleepNights and converts and formats it into one string for display.
 *
 * For display in a TextView, we have to supply one string, and styles are per TextView, not
 * applicable per word. So, we build a formatted string using HTML. This is handy, but we will
 * learn a better way of displaying this data in a future lesson.
 *
 * @param   nights - List of all SleepNights in the database.
 * @param   resources - Resources object for all the resources defined for our app.
 *
 * @return  Spanned - An interface for text that has formatting attached to it.
 *           See: https://developer.android.com/reference/android/text/Spanned
 */
fun formatNights(nights: List<SleepNight>, resources: Resources): Spanned {
    val sb = StringBuilder()
    sb.apply {
        append(resources.getString(R.string.title))
        nights.forEach {
            append("<br>")
            append(resources.getString(R.string.start_time))
            append(" ${convertLongToDateString(it.startTimeMillis)}<br>")
            if (it.endTimeMillis != it.startTimeMillis) {
                append(resources.getString(R.string.end_time))
                append(" ${convertLongToDateString(it.endTimeMillis)}<br>")
                append(resources.getString(R.string.quality))
                append(" ${convertNumericQualityToString(it.sleepQuality)}<br>")
                append(resources.getString(R.string.hours_slept))
                // Hours
                append(" ${it.endTimeMillis.minus(it.startTimeMillis) / 1000 / 60 / 60}:")
                // Minutes
                append("${it.endTimeMillis.minus(it.startTimeMillis) / 1000 / 60}:")
                // Seconds
                append("${it.endTimeMillis.minus(it.startTimeMillis) / 1000}<br><br>")
            }
        }
    }
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
    } else {
        HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}
