package com.example.android.trackmysleepquality.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.trackmysleepquality.data.Night
import com.example.android.trackmysleepquality.data.NightsDao
import com.example.android.trackmysleepquality.data.NightsDatabase
import com.example.android.trackmysleepquality.util.hash
import com.example.android.trackmysleepquality.util.singleArgViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class DetailsViewModel(private val nightId: Long) : ViewModel(), AnkoLogger {

    init { info("$hash new instance") }

    companion object {
        val FACTORY = singleArgViewModelFactory(::DetailsViewModel)
    }

    private val dao = NightsDatabase.instance.nightsDao

    val night = MediatorLiveData<Night>()

    init {
        viewModelScope.launch {
            night.addSource(dao { getAsLive(nightId) }, night::setValue)
        }
    }


    private suspend fun <T> dao(block: NightsDao.()->T) = withContext(Dispatchers.IO) { dao.block() }

}
