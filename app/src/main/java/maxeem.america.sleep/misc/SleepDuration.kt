package maxeem.america.sleep.misc

import android.icu.text.MeasureFormat.FormatWidth
import android.icu.text.MeasureFormat.getInstance
import android.icu.util.Measure
import android.icu.util.MeasureUnit
import android.os.Build
import android.text.format.DateUtils.*
import androidx.annotation.RequiresApi
import maxeem.america.sleep.R
import maxeem.america.sleep.ext.asQuantityString
import java.util.*
import kotlin.math.max
import kotlin.math.roundToInt

object SleepDuration {

    fun format(millis: Long) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
             formatImplV24(millis) else formatImpl(millis)

    @RequiresApi(Build.VERSION_CODES.N)
    private fun formatImplV24(millis: Long, formatWidth: FormatWidth = FormatWidth.WIDE): String {
        val formatter = getInstance(Locale.getDefault(), formatWidth)
        return when {
            millis >= HOUR_IN_MILLIS -> formatter.format(Measure(hoursIn(millis), MeasureUnit.HOUR))
            millis >= MINUTE_IN_MILLIS -> formatter.format(Measure(minutesIn(millis), MeasureUnit.MINUTE))
            else -> formatter.format(Measure(secondsIn(millis), MeasureUnit.SECOND))
        }
    }

    private fun formatImpl(millis: Long) = when(millis) {
        in 0 until MINUTE_IN_MILLIS ->
            secondsIn(millis).let { R.plurals.seconds.asQuantityString(it, it) }
        in MINUTE_IN_MILLIS until HOUR_IN_MILLIS ->
            minutesIn(millis).let { R.plurals.minutes.asQuantityString(it, it) }
        else ->
            hoursIn(millis).let { R.plurals.hours.asQuantityString(it, it) }
    }

    private fun hoursIn(millis: Long)   = ((millis + MINUTE_IN_MILLIS.times(30)) / HOUR_IN_MILLIS).toInt()
    private fun minutesIn(millis: Long) = ((millis + SECOND_IN_MILLIS.times(30)) / MINUTE_IN_MILLIS).toInt()
    private fun secondsIn(millis: Long) = (max(1000, millis).toFloat() / SECOND_IN_MILLIS).roundToInt()

}
