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

package com.example.android.trackmysleepquality.sleeptracker

import android.app.Application
import androidx.lifecycle.*
import com.example.android.trackmysleepquality.database.*
import com.example.android.trackmysleepquality.formatNights
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SleepTrackerViewModel(private val app: Application, state: SavedStateHandle) : AndroidViewModel(app) {

    private val dao     = SleepDatabase.instance.sleepDatabaseDao
    private val nights  = dao.getAll()
    private val tonight = MutableLiveData<SleepNight?>()

    init {
        viewModelScope.launch { tonight.value = dao { getTonight()?.takeIf { it.isActive() } } }
    }

    val nightsStr = Transformations.map(nights) { nights -> formatNights(nights, app.resources) }

    fun onStart() = viewModelScope.launch {
        if (tonight.value != null && tonight.value!!.isActive()) return@launch
        dao { insert(SleepNight()) }
        tonight.value = dao { getTonight() }
    }
    fun onStop() = viewModelScope.launch {
        if (tonight.value == null || !tonight.value!!.isActive()) return@launch
        tonight.value!!.wakeup()
        dao { update(tonight.value!!) }
    }
    fun onClear() = viewModelScope.launch {
        if (tonight.value == null) return@launch
        dao { clear() }
        tonight.value = null
    }

    private suspend fun <T> dao(block: SleepDatabaseDao.()->T) = withContext(Dispatchers.IO) { dao.block() }

}

