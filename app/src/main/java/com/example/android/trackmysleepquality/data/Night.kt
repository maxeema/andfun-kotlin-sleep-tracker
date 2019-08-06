/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.data

import android.text.format.DateUtils.*
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.app

@Entity(tableName = "nights")
data class Night(

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "night_id")
        var nightId         : Long = 0L,

        @ColumnInfo(name = "start_time_millis")
        val startTimeMillis : Long = System.currentTimeMillis(),

        @ColumnInfo(name = "end_time_millis")
        var endTimeMillis   : Long = startTimeMillis,

        @ColumnInfo(name = "quality_rating")
        var sleepQuality    : Int = -1

)

fun Night.isActive() = startTimeMillis == endTimeMillis
fun Night.wakeup() { endTimeMillis = System.currentTimeMillis() }

private val qualityNames = arrayOf(
        R.string.zero_very_bad, R.string.one_poor, R.string.two_soso,
        R.string.three_ok, R.string.four_pretty_good, R.string.five_excellent)
fun Night.describe() =
        if (sleepQuality in qualityNames.indices) app.getString(qualityNames[sleepQuality])
        else if (isActive()) "zzz...  " else "- - -    "

private val qualityImages = arrayOf(
        R.drawable.ic_sleep_0, R.drawable.ic_sleep_1, R.drawable.ic_sleep_2,
        R.drawable.ic_sleep_3, R.drawable.ic_sleep_4, R.drawable.ic_sleep_5
)
fun Night.present() = when (sleepQuality) {
        in qualityImages.indices -> qualityImages[sleepQuality]
        else -> if (isActive()) R.drawable.ic_sleep_active else R.drawable.ic_sleep_unspecified
}

private const val logFlags = FORMAT_ABBREV_ALL.or(FORMAT_SHOW_DATE).or(FORMAT_SHOW_TIME)
fun Night.log() = "%s - %s".format(
        formatDateTime(app, startTimeMillis, logFlags),
        if (isActive()) "..." else formatDateTime(app, endTimeMillis, logFlags))
