package maxeem.america.sleep.ext

import android.text.format.DateUtils.*
import maxeem.america.sleep.app
import maxeem.america.sleep.data.Night
import maxeem.america.sleep.misc.SleepDuration
import java.util.*

/**
 * Night extensions
 */

fun Night.isActive() = with (period) { startTime == endTime }

fun Night.wakeup() { period.endTime = System.currentTimeMillis() }

/**
 * Night period formatting
 */

private const val LOG_PERIOD_TIME_FLAGS = FORMAT_ABBREV_ALL or FORMAT_SHOW_TIME
private const val LOG_PERIOD_DATE_FLAGS = FORMAT_ABBREV_ALL or FORMAT_SHOW_DATE
private const val LOG_PERIOD_FLAGS = FORMAT_ABBREV_ALL or FORMAT_SHOW_DATE or FORMAT_SHOW_TIME // or FORMAT_SHOW_YEAR

private val sb = java.lang.StringBuilder(50)
private val formatter = Formatter(sb, Locale.ENGLISH)

fun Night.formattedPeriodStartTime() :String {
    sb.clear(); return formatDateRange(app, formatter, period.startTime, period.startTime, LOG_PERIOD_TIME_FLAGS).toString()
}
fun Night.formattedPeriodEndTime() :String {
    sb.clear(); return formatDateRange(app, formatter, period.endTime, period.endTime, LOG_PERIOD_TIME_FLAGS).toString()
}
fun Night.formattedPeriodStartDate() :String {
    sb.clear(); return formatDateRange(app, formatter, period.startTime, period.startTime, LOG_PERIOD_DATE_FLAGS).toString()
}
fun Night.formattedPeriodEndDate() :String {
    sb.clear(); return formatDateRange(app, formatter, period.endTime, period.endTime, LOG_PERIOD_DATE_FLAGS).toString()
}
fun Night.formattedPeriodLength() = SleepDuration.format(period.endTime - period.startTime)

fun Night.logPeriod() : String {
    sb.clear(); return formatDateRange(app, formatter, period.startTime, period.endTime, LOG_PERIOD_FLAGS).let {
        if (isActive()) "$it - …" else it.toString()
    }
}
fun Night.logPeriodEnd() :String {
    sb.clear(); return formatDateRange(app, formatter, period.endTime, period.endTime, LOG_PERIOD_DATE_FLAGS).toString()
}

