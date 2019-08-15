package maxeem.america.sleeping_journal.ext

import android.text.format.DateUtils.*
import maxeem.america.sleeping_journal.data.Night
import maxeem.america.sleeping_journal.misc.app

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
