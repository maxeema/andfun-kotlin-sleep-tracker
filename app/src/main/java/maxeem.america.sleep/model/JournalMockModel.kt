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

    companion object {
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
        var instance : JournalMockModel? = null
            private set
    }

    init {
        instance = this
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

    override fun onCleared() {
        super.onCleared()
        if (instance == this)
            instance = null
    }

    fun nightById(nightId: Long) = nights.value?.first { it.id == nightId}

}
