package com.example.android.trackmysleepquality.ext

import android.text.format.DateUtils.*
import com.example.android.trackmysleepquality.data.Night
import com.example.android.trackmysleepquality.misc.app

/**
 * Night extensions
 */

private const val LOG_FLAGS = FORMAT_ABBREV_ALL or FORMAT_SHOW_DATE or FORMAT_SHOW_TIME or FORMAT_SHOW_YEAR

fun Night.logPeriod() = formatDateRange(app, period.startTime, period.endTime, LOG_FLAGS).let {
    it + (if (isActive()) " - …" else "")
}

fun Night.isActive() = with (period) { startTime == endTime }

fun Night.wakeup() { period.endTime = System.currentTimeMillis() }
