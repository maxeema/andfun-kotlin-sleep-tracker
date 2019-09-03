package maxeem.america.sleep.model

import androidx.lifecycle.map
import maxeem.america.sleep.R
import maxeem.america.sleep.data.Night
import maxeem.america.sleep.ext.asMutable
import maxeem.america.sleep.misc.Prefs
import org.jetbrains.anko.info

class JournalRealModel : JournalModel() {

    override val nights    = dao.getAll()
    override val hasNights = nights.map { !it.isNullOrEmpty() }

    override fun doSleep() = action {
        val insertedId = dao { insert(Night()) } // mock()
        info(" inserted $insertedId")
        require(insertedId > 0) { "inserted id is $insertedId" }
        Prefs.apply {
            lastNightId = insertedId
            lastNightWasFinished = false
            lastNightHasToQualified = true
        }
        onComplete?.invoke(insertedId)
    }

    override fun deleteItem(item: Night) = action {
        val size = nights.value!!.size
        val deleted = dao { delete(item) }
        info(" deleted $deleted")
        require(1 == deleted) { "deleted $deleted of 1" }
        if (size == 1)
            Prefs.lastNightId = null
        messageEvent.asMutable().value = MessageEvent.Info(R.string.deleted_item_message)
    }

    override fun clearData() = action {
        val size = nights.value!!.size
        val cleared = dao { clear() }
        info(" cleared $cleared of $size")
        require(size == cleared) { "cleared $cleared of $size" }
        Prefs.lastNightId = null
        messageEvent.asMutable().value = MessageEvent.Info(R.string.cleared_data_message)
    }

}
