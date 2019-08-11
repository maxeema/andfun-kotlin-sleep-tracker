package com.example.android.trackmysleepquality.data

import com.example.android.trackmysleepquality.ext.asString
import com.example.android.trackmysleepquality.ext.logPeriod

object Converter {

    @JvmStatic fun nightToPeriodText(night: Night?) =
            night?.run { logPeriod() } ?: ""

    @JvmStatic fun nightToQualityText(night: Night?) =
        night?.run { quality.label.asString() } ?: ""

}