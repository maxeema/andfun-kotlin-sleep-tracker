package maxeem.america.sleep.model

import androidx.lifecycle.map
import maxeem.america.sleep.R
import maxeem.america.sleep.data.Night
import maxeem.america.sleep.ext.asMutable
import maxeem.america.sleep.misc.Consumable
import maxeem.america.sleep.misc.Prefs
import org.jetbrains.anko.info

class JournalModel : BaseModel() {

    val nights    = dao.getAll()
    val hasNights = nights.map { !it.isNullOrEmpty() }

    val hasData = Prefs.hasData

    fun doSleep() = action {
        val insertedId = dao.insert(Night())
        info(" inserted $insertedId")
        require(insertedId > 0) { "inserted id is $insertedId" }
        Prefs.apply {
            lastNightId = insertedId
            lastNightWasFinished = false
            lastNightHasToQualified = true
        }
        onComplete?.invoke(insertedId)
    }

    fun deleteItem(item: Night) = action {
        val size = nights.value!!.size
        val deleted = dao.delete(item)
        info(" deleted $deleted")
        require(1 == deleted) { "deleted $deleted of 1" }
        if (size == 1)
            Prefs.lastNightId = null
        messageEvent.asMutable().value = Consumable of MessageEvent.Info(R.string.deleted_item_message)
    }

    fun clearData() = action {
        val size = nights.value!!.size
        val cleared = dao.clear()
        info(" cleared $cleared of $size")
        require(size == cleared) { "cleared $cleared of $size" }
        Prefs.lastNightId = null
        messageEvent.asMutable().value = Consumable of MessageEvent.Info(R.string.cleared_data_message)
    }

}
