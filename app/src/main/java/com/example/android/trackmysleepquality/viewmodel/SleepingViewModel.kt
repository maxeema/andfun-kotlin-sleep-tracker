package com.example.android.trackmysleepquality.viewmodel

import com.example.android.trackmysleepquality.ext.singleArgViewModelFactory
import com.example.android.trackmysleepquality.ext.wakeup
import com.example.android.trackmysleepquality.misc.Prefs
import org.jetbrains.anko.info

class SleepingViewModel(private val nightId: Long) : BaseViewModel() {

    companion object {
        val FACTORY = singleArgViewModelFactory(::SleepingViewModel)
    }

    val night = dao.getAsLive(nightId)

    var onTap : (()->Unit)? = null

    fun onWakeUp() = action {
        val updated = dao { get(nightId)!!.run { wakeup(); update(this) } }
        info(" updated $updated")
        require(updated == 1) { "updated $updated of 1" }
        Prefs.lastNightActive = false
        onComplete?.invoke()
    }.let { true }

}
