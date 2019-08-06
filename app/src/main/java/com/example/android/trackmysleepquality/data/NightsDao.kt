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
    fun insert(item: Night) : Long

    @Update
    fun update(item: Night) : Int

    @Delete
    fun delete(vararg item: Night) : Int

    @Query("DELETE FROM nights")
    fun clear() : Int

    @Query("SELECT * from nights WHERE id = :nightId")
    fun get(nightId: Long) : Night?

    @Query("SELECT * from nights WHERE id = :nightId")
    fun getAsLive(nightId: Long) : LiveData<Night>

    @Query("SELECT * FROM nights ORDER BY id DESC")
    fun getAll() : LiveData<List<Night>>

    @Query("UPDATE nights SET quality = :quality WHERE id = :nightId")
    fun updateQuality(nightId: Long, quality: Int) : Int

    @Query("SELECT * FROM nights ORDER BY id DESC LIMIT 1")
    fun getTonight() : Night?

    @Query("SELECT * FROM nights ORDER BY id DESC LIMIT 1")
    fun getTonightLive() : LiveData<Night?>

}
