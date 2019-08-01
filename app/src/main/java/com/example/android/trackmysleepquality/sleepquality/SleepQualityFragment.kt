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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.databinding.FragmentSleepQualityBinding
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * Fragment that displays a list of clickable icons,
 * each representing a sleep quality rating.
 * Once the user taps an icon, the quality is set in the current sleepNight
 * and the database is updated.
 */
class SleepQualityFragment : Fragment(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        info("${hashCode()} onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        info("${hashCode()} onCreateView")

        val model by viewModels<SleepQualityViewModel>(factoryProducer = {
            SleepQualityViewModel.FACTORY(SleepQualityFragmentArgs.fromBundle(requireArguments()).sleepNightKey)
        })
        val binding = DataBindingUtil.inflate<FragmentSleepQualityBinding>(inflater, R.layout.fragment_sleep_quality, container, false)

        binding.lifecycleOwner = this
        binding.model = model

        model.navigateBack.observe(this, Observer {
            info("navigateBack observing $it")
            if (it) findNavController().popBackStack()
        })

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        info("${hashCode()} onDestroy")
    }

}
