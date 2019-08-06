package com.example.android.trackmysleepquality.util

import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.app
import com.example.android.trackmysleepquality.data.Night

val Any.hash get() = hashCode()

fun <T> MutableLiveData<T>.asImmutable() = this as LiveData<T>
fun <T> LiveData<T>.asMutable()          = this as MutableLiveData<T>

fun <T : ViewModel, A> singleArgViewModelFactory(constructor: (A) -> T):
        (A) -> ViewModelProvider.NewInstanceFactory {
    return { arg: A ->
        object : ViewModelProvider.NewInstanceFactory() {
            @Suppress("UNCHECKED_CAST")
            override fun <V : ViewModel> create(modelClass: Class<V>): V {
                return constructor(arg) as V
            }
        }
    }
}

fun Night.isActive() = with (period) { startTime == endTime }
fun Night.wakeup() { period.endTime = System.currentTimeMillis() }

private val qualityNames = arrayOf(
        R.string.zero_very_bad, R.string.one_poor, R.string.two_soso,
        R.string.three_ok, R.string.four_pretty_good, R.string.five_excellent)
fun Night.describe() = when (quality)  {
    in qualityNames.indices -> app.getString(qualityNames[quality])
    else -> if (isActive()) "zzz...  " else "- - -    " }

private val qualityImages = arrayOf(
        R.drawable.ic_sleep_0, R.drawable.ic_sleep_1, R.drawable.ic_sleep_2,
        R.drawable.ic_sleep_3, R.drawable.ic_sleep_4, R.drawable.ic_sleep_5
)
fun Night.present() = when (quality) {
    in qualityImages.indices -> qualityImages[quality]
    else -> if (isActive()) R.drawable.ic_sleep_active else R.drawable.ic_sleep_unspecified
}

private const val logFlags = DateUtils.FORMAT_ABBREV_ALL.or(DateUtils.FORMAT_SHOW_DATE).or(DateUtils.FORMAT_SHOW_TIME).or(DateUtils.FORMAT_SHOW_YEAR)
fun Night.log() = DateUtils.formatDateRange(app, period.startTime, period.endTime, logFlags)
