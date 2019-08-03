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

package com.example.android.trackmysleepquality.database

import android.text.format.DateUtils.*
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.trackmysleepquality.app
import com.example.android.trackmysleepquality.convertNumericQualityToImage
import com.example.android.trackmysleepquality.convertNumericQualityToString


@Entity(tableName = TABLE_SLEEP_QUALITY)
data class SleepNight(

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

fun SleepNight.isActive() = startTimeMillis == endTimeMillis
fun SleepNight.wakeup() { endTimeMillis = System.currentTimeMillis() }

val SleepNight.icon get() = convertNumericQualityToImage(sleepQuality)
val SleepNight.how  get() = convertNumericQualityToString(sleepQuality)
val SleepNight.date get() = "%s - %s".format(
        formatDateTime(app, startTimeMillis, FORMAT_ABBREV_ALL.or(FORMAT_SHOW_DATE).or(FORMAT_SHOW_TIME)),
            if (isActive()) "..." else formatDateTime(app, endTimeMillis, FORMAT_ABBREV_ALL.or(FORMAT_SHOW_DATE).or(FORMAT_SHOW_TIME)))
