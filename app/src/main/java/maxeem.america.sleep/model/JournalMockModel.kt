package maxeem.america.sleep.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import maxeem.america.sleep.data.Night
import maxeem.america.sleep.ext.asImmutable
import maxeem.america.sleep.ext.asMutable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class JournalMockModel : JournalModel() {

    private companion object {
        val format = SimpleDateFormat("yy-MM-dd HH:mm", Locale.ENGLISH)
        const val data = """
                5, 19-08-25 22:45 to 19-08-26 05:45
                4, 19-08-26 12:15 to 19-08-26 12:45
                3, 19-08-26 23:15 to 19-08-27 05:45
                4, 19-08-27 22:15 to 19-08-28 06:00
                4, 19-08-28 12:00 to 19-08-28 13:00
                2, 19-08-28 23:30 to 19-08-29 06:00
                5, 19-08-29 12:30 to 19-08-29 13:00
                5, 19-08-30 22:15 to 19-08-31 05:45
                3, 19-08-31 23:00 to 19-09-01 06:00
                4, 19-09-01 12:00 to 19-09-01 13:00
                5, 19-09-01 23:00 to 19-09-02 05:45
                4, 19-09-02 22:45 to 19-09-03 05:30
                3, 19-09-03 12:15 to 19-09-03 13:00
                5, 19-09-03 22:30 to 19-09-04 06:00
                5, 19-09-04 22:45 to 19-09-05 05:45
            """
    }

    override val nights = MutableLiveData(
        data.trimIndent().split("\n").map {
            val info = it.split(", ", " to ")
            Night(id = System.currentTimeMillis()+Random.nextInt(), quality = Night.Quality.of(info[0].toInt()),
                    period=Night.Period(format.parse(info[1]).time, format.parse(info[2]).time))
        }.reversed()
    ).asImmutable()

    override val hasNights = nights.map { it.isNotEmpty() }

    override fun deleteItem(item: Night) {
        nights.asMutable().value = nights.value!!.toMutableList().apply { remove(item) }
    }

    override fun clearData() {
        nights.asMutable().value = emptyList()
    }

}
