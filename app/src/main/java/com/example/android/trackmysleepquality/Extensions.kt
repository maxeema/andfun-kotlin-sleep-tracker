package com.example.android.trackmysleepquality

import android.graphics.drawable.Drawable
import android.text.format.DateUtils.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.database.isActive
import java.lang.ref.SoftReference

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

private val nightQualityNames = arrayOf(
        R.string.zero_very_bad,   R.string.one_poor,           R.string.two_soso,
        R.string.three_ok,        R.string.four_pretty_good,   R.string.five_excellent)
fun SleepNight.describeIt() = if (sleepQuality in nightQualityNames.indices) app.getString(nightQualityNames[sleepQuality]) else  "- - -"

private val nightQualityImgs = arrayOfNulls<SoftReference<Drawable>>(nightQualityNames.size)
private val nightQualityImgActive by lazy { app.getDrawable(R.drawable.ic_sleep_active)!! }
private val nightQualityImgUnspecified by lazy { app.getDrawable(R.drawable.ic_sleep_unspecified)!! }
fun SleepNight.presentIt() = when (sleepQuality) {
    in nightQualityNames.indices -> nightQualityImgs[sleepQuality]?.get()
        ?: app.getDrawable(app.resources.getIdentifier("ic_sleep_$sleepQuality", "drawable", app.packageName))!!.apply {
            nightQualityImgs[sleepQuality] = SoftReference(this)
        }
    else -> if (isActive()) nightQualityImgActive else nightQualityImgUnspecified
}

fun SleepNight.logIt() = "%s - %s".format(
        formatDateTime(app, startTimeMillis, FORMAT_ABBREV_ALL.or(FORMAT_SHOW_DATE).or(FORMAT_SHOW_TIME)),
        if (isActive()) "..." else formatDateTime(app, endTimeMillis, FORMAT_ABBREV_ALL.or(FORMAT_SHOW_DATE).or(FORMAT_SHOW_TIME)))