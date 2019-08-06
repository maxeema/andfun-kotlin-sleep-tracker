package com.example.android.trackmysleepquality.viewmodel

import androidx.lifecycle.ViewModel
import com.example.android.trackmysleepquality.data.NightsDatabase
import com.example.android.trackmysleepquality.util.hash
import com.example.android.trackmysleepquality.util.singleArgViewModelFactory
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class DetailsViewModel(private val nightId: Long) : ViewModel(), AnkoLogger {

    init { info("$hash new instance") }

    companion object {
        val FACTORY = singleArgViewModelFactory(::DetailsViewModel)
    }

    val night = NightsDatabase.instance.nightsDao.getAsLive(nightId)

}
