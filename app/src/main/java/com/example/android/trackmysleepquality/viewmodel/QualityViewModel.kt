package com.example.android.trackmysleepquality.viewmodel

import com.example.android.trackmysleepquality.data.Night
import com.example.android.trackmysleepquality.ext.singleArgViewModelFactory

class QualityViewModel(private val nightId: Long) : BaseViewModel() {

    companion object {
        val FACTORY = singleArgViewModelFactory(::QualityViewModel)
    }

    val night = dao.tonightAsLive()

    fun onSetQuality(quality: Night.Quality) = action {
        val updated = dao { updateQuality(nightId, quality) }
        require(updated == 1) { "updated $updated of 1" }
        onComplete?.let { it() }
    }

}
