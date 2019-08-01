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

package com.example.android.trackmysleepquality.sleepquality

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.trackmysleepquality.asImmutable
import com.example.android.trackmysleepquality.asMutable
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.singleArgViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SleepQualityViewModel(private val sleepNightKey: Long) : ViewModel() {

    companion object {
        val FACTORY = singleArgViewModelFactory(::SleepQualityViewModel)
    }

    private val dao = SleepDatabase.instance.sleepDatabaseDao

    val navigateBack = MutableLiveData<Boolean>().asImmutable()

    fun onSetSleepQuality(value: Int) = viewModelScope.launch {
        val night = dao { get(sleepNightKey) } ?: return@launch
        night.sleepQuality = value
        dao { update(night) }
        navigateBack.asMutable().value = true
    }

    private suspend fun <T> dao(block: SleepDatabaseDao.()->T) = withContext(Dispatchers.IO) { dao.block() }
}
