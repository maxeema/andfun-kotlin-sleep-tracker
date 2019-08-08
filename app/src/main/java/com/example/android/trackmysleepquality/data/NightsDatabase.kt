package com.example.android.trackmysleepquality.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.android.trackmysleepquality.misc.DATABASE_NAME
import com.example.android.trackmysleepquality.misc.app

@Database(
        entities = [Night::class],
        version = 13,
        exportSchema = false
)
@TypeConverters(ConvertTypes::class)
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
