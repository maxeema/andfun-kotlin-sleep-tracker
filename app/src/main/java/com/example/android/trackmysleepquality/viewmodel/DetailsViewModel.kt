package com.example.android.trackmysleepquality.viewmodel

import com.example.android.trackmysleepquality.ext.singleArgViewModelFactory

class DetailsViewModel(nightId: Long) : BaseViewModel() {

    companion object {
        val FACTORY = singleArgViewModelFactory(::DetailsViewModel)
    }

    val night = dao.getAsLive(nightId)

}
