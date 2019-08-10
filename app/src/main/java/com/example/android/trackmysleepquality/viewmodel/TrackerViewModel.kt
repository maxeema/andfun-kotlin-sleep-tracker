package com.example.android.trackmysleepquality.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.data.Night
import com.example.android.trackmysleepquality.ext.asMutable
import com.example.android.trackmysleepquality.misc.Prefs
import org.jetbrains.anko.info

class TrackerViewModel(state: SavedStateHandle) : BaseViewModel() {

    val nights = dao.getAll()
    val hasNights : LiveData<Boolean?> = Transformations.map(nights) { !it.isNullOrEmpty() }

    fun onDoSleep() = action {
        val inserted = dao { insert(Night()) }
        info(" inserted $inserted")
        require(inserted > 0) { "inserted id is $inserted"}
        Prefs.apply {
            lastNightId = inserted
            lastNightActive = true
        }
        onComplete?.invoke()
    }
    fun onClearData() = action {
        val size = nights.value!!.size
        val cleared = dao { clear() }
        info(" cleared $cleared of $size")
        require(size == cleared) { "cleared $cleared of $size" }
        Prefs.lastNightId = null
        messageEvent.asMutable().value = MessageEvent.Info(R.string.cleared_message)
    }

}

