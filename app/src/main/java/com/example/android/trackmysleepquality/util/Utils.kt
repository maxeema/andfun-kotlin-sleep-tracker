
package com.example.android.trackmysleepquality.util

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.text.format.DateUtils.*
import androidx.core.text.HtmlCompat
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.app
import com.example.android.trackmysleepquality.data.Night
import java.text.SimpleDateFormat

fun prepareDetailedError(msgId: Int, err: Throwable) =
        HtmlCompat.fromHtml(app.getString(msgId).plus("<br/><br/><small>[ ${err.javaClass.simpleName} ]").let {
            if (err.message.isNullOrBlank()) it else it.plus("<br/>${err.message}")
        }.plus("</small>"), HtmlCompat.FROM_HTML_MODE_COMPACT)

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
 * Takes a recycler of SleepNights and converts and formats it into one string for display.
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
fun formatNights(nights: List<Night>, resources: Resources): Spanned {
    val sb = StringBuilder()
    sb.apply {
        append(resources.getString(R.string.title))
        nights.forEach {
            append("<br>")
            append(resources.getString(R.string.start_time))
            append(" ${convertLongToDateString(it.period.startTime)}<br>")
            if (it.period.endTime != it.period.startTime) {
                append(resources.getString(R.string.end_time))
                append(" ${convertLongToDateString(it.period.endTime)}<br>")
                append(resources.getString(R.string.quality))
                append(" ${it.describe()}<br>")
                append(resources.getString(R.string.hours_slept))
                // Hours
                append(" ${it.period.endTime.minus(it.period.startTime) / HOUR_IN_MILLIS}:")
                // Minutes
                append("${it.period.endTime.minus(it.period.startTime) / MINUTE_IN_MILLIS}:")
                // Seconds
                append("${it.period.endTime.minus(it.period.startTime) / SECOND_IN_MILLIS}<br><br>")
            }
        }
    }
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
    } else {
        HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}
