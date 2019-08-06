package com.example.android.trackmysleepquality.data

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nights")
data class Night(

        @PrimaryKey
        @ColumnInfo(name = "id")
        var id                  : Long? = null,

        @ColumnInfo(name = "quality")
        var quality             : Int = -1,

        @Embedded(prefix = "period_")
        val period              : Period = Period()

) {
        data class Period(

                @ColumnInfo(name = "start")
                val startTime     : Long = System.currentTimeMillis(),

                @ColumnInfo(name = "end")
                var endTime       : Long = startTime

        )
}
