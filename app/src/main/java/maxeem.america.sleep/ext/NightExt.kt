package maxeem.america.sleep.ext

import android.text.format.DateUtils.*
import maxeem.america.sleep.app
import maxeem.america.sleep.data.Night

/**
 * Night extensions
 */

private const val LOG_PERIOD_FLAGS = FORMAT_ABBREV_ALL or FORMAT_SHOW_DATE or FORMAT_SHOW_TIME or FORMAT_SHOW_YEAR
private const val LOG_PERIOD_END_FLAGS = FORMAT_ABBREV_ALL or FORMAT_SHOW_DATE or FORMAT_SHOW_TIME

fun Night.logPeriod() = formatDateRange(app, period.startTime, period.endTime, LOG_PERIOD_FLAGS).plus(
    if (isActive()) " - …" else ""
)
fun Night.logPeriodEnd() = formatDateTime(app, period.endTime, LOG_PERIOD_END_FLAGS)

fun Night.isActive() = with (period) { startTime == endTime }

fun Night.wakeup() { period.endTime = System.currentTimeMillis() }
