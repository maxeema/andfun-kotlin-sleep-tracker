package maxeem.america.sleeping_journal.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import maxeem.america.sleeping_journal.R
import maxeem.america.sleeping_journal.data.Night
import maxeem.america.sleeping_journal.ext.asMutable
import maxeem.america.sleeping_journal.misc.Prefs
import org.jetbrains.anko.info

class JournalViewModel(state: SavedStateHandle) : BaseViewModel() {

    val nights = dao.getAll()
    val hasNights : LiveData<Boolean?> = Transformations.map(nights) { !it.isNullOrEmpty() }

    fun doSleep() = action {
        val inserted = dao { insert(Night()) }
        info(" inserted $inserted")
        require(inserted > 0) { "inserted id is $inserted" }
        Prefs.apply {
            lastNightId = inserted
            lastNightActive = true
            lastNightQualified = false
        }
        onComplete?.invoke(inserted)
    }
    fun deleteItem(item: Night) = action {
        val size = nights.value!!.size
        val deleted = dao { delete(item) }
        info(" deleted $deleted")
        require(1 == deleted) { "deleted $deleted of 1" }
        if (size == 1)
            Prefs.lastNightId = null
        messageEvent.asMutable().value = MessageEvent.Info(R.string.deleted_item_message)
    }
    fun clearData() = action {
        val size = nights.value!!.size
        val cleared = dao { clear() }
        info(" cleared $cleared of $size")
        require(size == cleared) { "cleared $cleared of $size" }
        Prefs.lastNightId = null
        messageEvent.asMutable().value = MessageEvent.Info(R.string.cleared_data_message)
    }

}
