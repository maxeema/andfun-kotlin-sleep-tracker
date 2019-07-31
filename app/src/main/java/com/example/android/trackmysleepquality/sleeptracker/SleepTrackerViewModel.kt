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
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import com.example.android.trackmysleepquality.database.*
import com.example.android.trackmysleepquality.formatNights
import kotlinx.coroutines.*

class SleepTrackerViewModel(private val app: Application, state: SavedStateHandle) : AndroidViewModel(app) {

    private val dao     = SleepDatabase.instance.sleepDatabaseDao
    private val job     = Job()
    private val scope   = CoroutineScope(Dispatchers.Main + job)
    private val nights  = dao.getAll()
    private val tonight = MutableLiveData<SleepNight?>()

    init {
        scope.launch { tonight.value = getTonight() }
    }

    val nightsStr = Transformations.map(nights) { nights -> formatNights(nights, app.resources) }

    fun onStart() = scope.launch {
        insert(SleepNight())
        tonight.value = getTonight()
    }
    fun onStop() = scope.launch {
        tonight.value!!.wakeup()
        update(tonight.value!!)
    }
    fun onClear() = scope.launch {
        clear(); tonight.value = null
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    private suspend fun getTonight()          = dao { getTonight()?.takeIf { it.isActive() } }
    private suspend fun insert(n: SleepNight) = dao { insert(n) }
    private suspend fun update(n: SleepNight) = dao { update(n) }
    private suspend fun clear()               = dao { clear() }

    private suspend fun <T> dao(block: SleepDatabaseDao.()->T) = withContext(Dispatchers.IO) { dao.block() }

}

