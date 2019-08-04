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
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.android.trackmysleepquality.databinding.FragmentQualityBinding
import com.example.android.trackmysleepquality.viewmodel.QualityViewModel
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class QualityFragment : Fragment(), AnkoLogger {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        info("${hashCode()} onCreateView")

        val model by viewModels<QualityViewModel> {
            QualityViewModel.FACTORY(QualityFragmentArgs.fromBundle(requireArguments()).nightId)
        }
        val binding = FragmentQualityBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.model = model

        model.navigateBack.observe(viewLifecycleOwner) {
            info("navigateBack observing $it")
            if (it) findNavController().popBackStack()
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
