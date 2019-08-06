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

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NightsDao {

    @Insert
    fun insert(item: Night)

    @Update
    fun update(item: Night)

    @Delete
    fun delete(item: Night)

    @Query("DELETE FROM nights")
    fun clear() : Int

    @Query("SELECT * from nights WHERE night_id = :id")
    fun get(id: Long) : Night?

    @Query("SELECT * from nights WHERE night_id = :id")
    fun getAsLive(id: Long) : LiveData<Night>

    @Query("SELECT * FROM nights ORDER BY night_id DESC")
    fun getAll() : LiveData<List<Night>>

    @Query("SELECT * FROM nights ORDER BY night_id DESC LIMIT 1")
    fun getTonight() : Night?

}
