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
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.asImmutable
import com.example.android.trackmysleepquality.asMutable
import com.example.android.trackmysleepquality.database.*
import com.example.android.trackmysleepquality.formatNights
import kotlinx.coroutines.*
import org.jetbrains.anko.AnkoLogger

class SleepTrackerViewModel(private val app: Application, state: SavedStateHandle) : AndroidViewModel(app), AnkoLogger {

    private val dao    = SleepDatabase.instance.sleepDatabaseDao
    private val nights = dao.getAll()

    init { viewModelScope.launch { tonight.asMutable().value = dao { getTonight() } } }

    val tonight   = MutableLiveData<SleepNight?>().asImmutable()
    val nightsStr = Transformations.map(nights) { formatNights(it, app.resources) }

    val isSleeping = Transformations.map(tonight) { it != null && it.isActive() }
    val hasNights  = Transformations.map(nights)  { it.isNotEmpty() }

    val messageEvent = MutableLiveData<CharSequence>().asImmutable()
    val qualifyEvent = MutableLiveData<SleepNight>().asImmutable()

    private var job : Job? = null
    private fun checkIsActive() = job?.isActive?.takeIf { it }
            ?.apply { messageEvent.asMutable().value = app.getString(R.string.please) } ?: false

    fun onDoSleep() = action {
        val restore = tonight.value
        tonight.asMutable().value = SleepNight()
        tonight.asMutable().value = runCatching {
            dao {
                insert(tonight.value!!)
                getTonight()
            }
        }.onFailure { err ->
            messageEvent.asMutable().value = app.getString(R.string.sorry, err)
        }.getOrElse { restore }
    }

    fun onWakeUp() = action {
        val restore = tonight.value!!.copy()
        runCatching {
            tonight.asMutable().value = tonight.value!!.apply { wakeup() }
            dao { update(tonight.value!!) }
        }.onSuccess {
            qualifyEvent.asMutable().value = tonight.value
        }.onFailure { err ->
            messageEvent.asMutable().value = app.getString(R.string.sorry, err)
            tonight.asMutable().value = restore
        }
    }
    fun onClearData() = action {
        val restore = tonight.value
        runCatching {
            tonight.asMutable().value = null
            dao { clear() }
        }.onSuccess {
            messageEvent.asMutable().value = app.getString(R.string.cleared_message)
        }.onFailure { err ->
            messageEvent.asMutable().value = app.getString(R.string.sorry, err)
            tonight.asMutable().value = restore
        }
    }

    private fun action(block: suspend CoroutineScope.()->Unit) {
        if (checkIsActive()) return
        viewModelScope.launch {
            this.block()
        }.apply {
            job = this
        }.invokeOnCompletion {
            job = null
        }
    }

    private suspend fun <T> dao(block: SleepDatabaseDao.()->T) = withContext(Dispatchers.IO) { dao.block() }

    fun qualifyEventConsumed() { qualifyEvent.asMutable().value = null }
    fun messageEventConsumed() { messageEvent.asMutable().value = null }

}

