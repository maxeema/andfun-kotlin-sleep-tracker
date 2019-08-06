/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.trackmysleepquality.data.NightsDao
import com.example.android.trackmysleepquality.data.NightsDatabase
import com.example.android.trackmysleepquality.util.asImmutable
import com.example.android.trackmysleepquality.util.asMutable
import com.example.android.trackmysleepquality.util.hash
import com.example.android.trackmysleepquality.util.singleArgViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class QualityViewModel(private val nightId: Long) : ViewModel(), AnkoLogger {

    init { info("$hash new instance") }

    companion object {
        val FACTORY = singleArgViewModelFactory(::QualityViewModel)
    }

    private val dao = NightsDatabase.instance.nightsDao

    val completeEvent = MutableLiveData<Boolean>().asImmutable()

    fun onSetSleepQuality(value: Int) = viewModelScope.launch {
        val night = dao { get(nightId) } ?: return@launch
        night.sleepQuality = value
        dao { update(night) }
        completeEvent.asMutable().value = true
    }

    private suspend fun <T> dao(block: NightsDao.()->T) = withContext(Dispatchers.IO) { dao.block() }

}
