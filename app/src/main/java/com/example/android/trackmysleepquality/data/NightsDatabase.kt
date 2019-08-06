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

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.android.trackmysleepquality.app
import com.example.android.trackmysleepquality.util.DATABASE_NAME

@Database(
        entities = [Night::class],
        version = 13,
        exportSchema = false
)
abstract class NightsDatabase : RoomDatabase() {

    abstract val nightsDao: NightsDao

    companion object {

        val instance by lazy {
            Room.databaseBuilder(app, NightsDatabase::class.java, DATABASE_NAME).run {
                fallbackToDestructiveMigration()
                build()
            }
        }

    }

}
