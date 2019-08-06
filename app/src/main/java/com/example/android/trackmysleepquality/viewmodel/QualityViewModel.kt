package com.example.android.trackmysleepquality.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.android.trackmysleepquality.util.asImmutable
import com.example.android.trackmysleepquality.util.asMutable
import com.example.android.trackmysleepquality.util.singleArgViewModelFactory

class QualityViewModel(private val nightId: Long) : BaseViewModel() {

    companion object {
        val FACTORY = singleArgViewModelFactory(::QualityViewModel)
    }

    val completeEvent = MutableLiveData<Boolean>().asImmutable()

    fun onSetQuality(quality: Int) = action {
        val updated = dao { updateQuality(nightId, quality) }
        require(updated == 1) { "updated $updated of 1" }
        completeEvent.asMutable().value = true
    }

}
