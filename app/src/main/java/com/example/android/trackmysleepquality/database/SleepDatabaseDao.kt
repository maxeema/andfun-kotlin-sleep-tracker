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

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SleepDatabaseDao {

    @Insert
    fun insert(item: SleepNight)

    @Update
    fun update(item: SleepNight)

    @Delete
    fun delete(item: SleepNight)

    @Query("DELETE FROM $TABLE_SLEEP_QUALITY")
    fun clear() : Int

    @Query("SELECT * from $TABLE_SLEEP_QUALITY WHERE night_id = :id")
    fun get(id: Long) : SleepNight?

    @Query("SELECT * FROM $TABLE_SLEEP_QUALITY ORDER BY night_id DESC")
    fun getAll() : LiveData<List<SleepNight>>

    @Query("SELECT * FROM $TABLE_SLEEP_QUALITY ORDER BY night_id DESC LIMIT 1")
    fun getTonight() : SleepNight?

}
