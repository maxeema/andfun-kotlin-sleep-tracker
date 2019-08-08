package com.example.android.trackmysleepquality.ext

import android.text.format.DateUtils.*
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.data.Night
import com.example.android.trackmysleepquality.misc.app

/**
 * Night extensions
 */

private const val LOG_FLAGS = FORMAT_ABBREV_ALL.or(FORMAT_SHOW_DATE).or(FORMAT_SHOW_TIME).or(FORMAT_SHOW_YEAR)

fun Night.isActive() = with (period) { startTime == endTime }
fun Night.wakeup() { period.endTime = System.currentTimeMillis() }

fun Night.describe() = when {
        isActive() -> R.string.zzz
              else -> quality.label
}

fun Night.present() = when {
        isActive() -> R.drawable.ic_sleep_active
              else -> quality.img
}

fun Night.log() = formatDateRange(app, period.startTime, period.endTime, LOG_FLAGS).let {
    it + (if (isActive()) " - …" else "")
}

fun Night?.requireId() = requireNotNull(requireNotNull(this).id)
