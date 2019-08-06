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
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.adapter.TrackerAdapter
import com.example.android.trackmysleepquality.data.Night
import com.example.android.trackmysleepquality.databinding.FragmentTrackerBinding
import com.example.android.trackmysleepquality.util.hash
import com.example.android.trackmysleepquality.viewmodel.TrackerViewModel
import com.google.android.material.snackbar.Snackbar
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class TrackerFragment : Fragment(), AnkoLogger {

    private lateinit var model : TrackerViewModel
    private var clear : MenuItem? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        info("$hash onCreateView, $savedInstanceState")

        model = viewModels<TrackerViewModel>{ SavedStateViewModelFactory(this, savedInstanceState) }.value

        val binding = FragmentTrackerBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.model = model

        val adapter = TrackerAdapter().apply {
            binding.recycler.adapter = this
        }
        adapter.onItemClick = View.OnClickListener { val night = it.tag as Night
            findNavController().navigate(TrackerFragmentDirections.actionTrackerFragToDetailsFrag(night.nightId))
        }
        adapter.registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                binding.recycler.scrollToPosition(0)
            }
        })
        binding.recycler.layoutManager.let { it as GridLayoutManager }.apply {
            spanCount = resources.getInteger(R.integer.grid_span_count)
        }
        model.nights.observe(viewLifecycleOwner) { nights ->
            info((" nights update to -> $nights"))
            adapter.submitList(nights.takeIf { it.isNotEmpty()}) // take null to clear instead of empty list
            clear?.isVisible = nights.isNotEmpty()
        }
        model.qualifyEvent.observe(viewLifecycleOwner) { night ->
            night ?: return@observe
            binding.recycler.scrollToPosition(0)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.tracker_menu, menu)
        clear = menu.findItem(R.id.clear).apply {
            isVisible = model.hasNights.value ?: false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId) {
        R.id.clear  -> { model.onClearData(); item.isVisible = false; true }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        info("$hash onDestroyView")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        info("$hash onCreate")
        setHasOptionsMenu(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        info("$hash onDestroy")
    }

}
