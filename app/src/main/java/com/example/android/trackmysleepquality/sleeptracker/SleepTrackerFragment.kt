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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.navigation.fragment.findNavController
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.databinding.FragmentSleepTrackerBinding
import com.google.android.material.snackbar.Snackbar
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * A fragment with buttons to record start and end times for sleep, which are saved in a database.
 * Cumulative data is displayed in a simple scrollable TextView.
 */
class SleepTrackerFragment : Fragment(), AnkoLogger {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        info("${hashCode()} onCreateView, $savedInstanceState")

        val model by viewModels<SleepTrackerViewModel>( factoryProducer = { SavedStateViewModelFactory(this, savedInstanceState) } )
        val binding = DataBindingUtil.inflate<FragmentSleepTrackerBinding>(inflater, R.layout.fragment_sleep_tracker, container, false)

        binding.lifecycleOwner = this
        binding.model = model

        model.tonight.observe(this, Observer {
            it?.apply { binding.scroll.smoothScrollTo(0, 0) }
        })
        model.qualifyEvent.observe(this, Observer { night ->
            if (night == null) return@Observer
            binding.scroll.scrollTo(0, 0)
            findNavController().navigate(SleepTrackerFragmentDirections.actionSleepTrackerFragmentToSleepQualityFragment(night.nightId))
            model.qualifyEventConsumed()
        })
        model.messageEvent.observe(this, Observer { msg ->
            if (msg == null) return@Observer
            Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG).show()
            model.messageEventConsumed()
        })

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        info("${hashCode()} onCreate")
    }

    override fun onDestroy() {
        super.onDestroy()
        info("${hashCode()} onDestroy")
    }

}
