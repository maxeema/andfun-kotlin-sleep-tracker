package com.example.android.trackmysleepquality.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.data.Night
import com.example.android.trackmysleepquality.util.*
import org.jetbrains.anko.info

class TrackerViewModel(state: SavedStateHandle) : BaseViewModel() {

    val nights = dao.getAll()
    val tonight = dao.getTonightLive()

    val isSleeping = Transformations.map(tonight) { it?.isActive() ?: false }
    val hasNights  = Transformations.map(nights)  { it.isNotEmpty() }

    val qualifyEvent = MutableLiveData<Night?>().asImmutable()

    fun onDoSleep() = action {
        val inserted = dao { insert(Night()) }
        info(" inserted $inserted")
        require(inserted > 0) { "inserted id is $inserted"}
        Prefs.lastNight = inserted
    }
    fun onWakeUp() = action {
        val updated = dao { update(tonight.value!!.copy().apply { wakeup() }) }
        info(" updated $updated")
        require(updated == 1) { "updated $updated of 1" }
        qualifyEvent.asMutable().value = tonight.value
    }
    fun onClearData() = action {
        val size = nights.value!!.size
        val cleared = dao { clear() }
        info(" cleared $cleared of $size")
        require(size == cleared) { "cleared $cleared of $size" }
        Prefs.lastNight = null
        messageEvent.asMutable().value = MessageEvent.Info(R.string.cleared_message)
    }

    fun qualifyEventConsumed() { qualifyEvent.asMutable().value = null }

}

