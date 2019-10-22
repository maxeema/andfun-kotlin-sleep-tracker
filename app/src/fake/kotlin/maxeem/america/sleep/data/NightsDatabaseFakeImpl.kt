package maxeem.america.sleep.data

import androidx.room.Room
import maxeem.america.sleep.app
import maxeem.america.sleep.misc.Prefs
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

abstract class NightsDatabaseFakeImpl : NightsDatabase() {

    companion object {
        val instance by lazy {
            Room.inMemoryDatabaseBuilder(app, NightsDatabase::class.java)
                    .allowMainThreadQueries()
                    .build().apply {
                data.trimIndent().split("\n").map {
                    val info = it.split(", ", " to ")
                    Night(id = System.currentTimeMillis()+ Random.nextInt(), quality = Night.Quality.of(info[0].toInt()),
                            period=Night.Period(format.parse(info[1]).time, format.parse(info[2]).time))
                }.reversed().also {
                    val ids = nightsDao.insert(*it.toTypedArray())
                    Prefs.lastNightId = ids.last()
                }
            }
        }

        private val format = SimpleDateFormat("yy-MM-dd HH:mm", Locale.ENGLISH)

        private const val data = """
            5, 19-09-25 22:45 to 19-09-26 05:45
            4, 19-09-26 12:15 to 19-09-26 12:45
            3, 19-09-26 23:15 to 19-09-27 05:45
            4, 19-09-27 22:15 to 19-09-28 06:00
            4, 19-09-28 12:00 to 19-09-28 13:00
            2, 19-09-28 23:30 to 19-09-29 06:00
            5, 19-09-29 12:30 to 19-09-29 13:00
            5, 19-09-30 22:15 to 19-09-31 05:45
            3, 19-09-31 23:00 to 19-10-01 06:00
            4, 19-10-01 12:00 to 19-10-01 13:00
            5, 19-10-01 23:00 to 19-10-02 05:45
            4, 19-10-02 22:45 to 19-10-03 05:30
            3, 19-10-03 12:15 to 19-10-03 13:00
            5, 19-10-03 22:30 to 19-10-04 06:00
            5, 19-10-04 22:45 to 19-10-05 05:45
        """
    }

}
