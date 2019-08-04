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

package com.example.android.trackmysleepquality

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.adapter.TrackerAdapter
import com.example.android.trackmysleepquality.databinding.FragmentTrackerBinding
import com.example.android.trackmysleepquality.viewmodel.TrackerViewModel
import com.google.android.material.snackbar.Snackbar
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class TrackerFragment : Fragment(), AnkoLogger {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        info("${hashCode()} onCreateView, $savedInstanceState")

        val model : TrackerViewModel by viewModels { SavedStateViewModelFactory(this, savedInstanceState) }
        val binding = FragmentTrackerBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.model = model

        val adapter = TrackerAdapter().apply {
            binding.sleepList.adapter = this
            registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver() {
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    binding.sleepList.scrollToPosition(0)
                }
            })
        }

        model.nights.observe(viewLifecycleOwner) { nights ->
            adapter.submitList(nights)
        }
        model.qualifyEvent.observe(viewLifecycleOwner) { night ->
            night ?: return@observe
            binding.sleepList.scrollToPosition(0)
            findNavController().navigate(TrackerFragmentDirections.actionTrackerFragToQualityFrag(night.nightId))
            model.qualifyEventConsumed()
        }
        model.messageEvent.observe(viewLifecycleOwner) { msg ->
            msg ?: return@observe
            Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT).show()
            model.messageEventConsumed()
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        info("${hashCode()} onDestroyView")
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
