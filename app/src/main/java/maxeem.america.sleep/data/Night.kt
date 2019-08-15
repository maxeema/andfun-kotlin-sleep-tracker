package maxeem.america.sleep.data

import androidx.annotation.StringRes
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import maxeem.america.sleep.R

@Entity(tableName = "nights")
data class Night(

        @PrimaryKey
        @ColumnInfo(name = "id")
        var id                  : Long? = null, // let SQLite provide ID

        @ColumnInfo(name = "quality")
        var quality             : Quality = Quality.UNDEFINED,

        @Embedded(prefix = "period_")
        val period              : Period = Period()

) {
        data class Period(

                @ColumnInfo(name = "start")
                val startTime     : Long = System.currentTimeMillis(),

                @ColumnInfo(name = "end")
                var endTime       : Long = startTime

        )

        enum class Quality(val id: Int, @StringRes val label: Int, @StringRes val img: Int) {
                UNDEFINED(-1, R.string.undefined, R.drawable.undefined),
                VERY_BAD(0, R.string.very_bad, R.drawable.very_bad),
                POOR(1, R.string.poor, R.drawable.poor),
                SO_SO(2, R.string.so_so, R.drawable.so_so),
                OK(3, R.string.ok, R.drawable.ok),
                PRETTY_GOOD(4, R.string.pretty_good, R.drawable.pretty_good),
                EXCELLENT(5, R.string.excellent, R.drawable.excellent);

                companion object {
                        fun of(id: Int) = values().first { it.id == id }
                }
        }
}

