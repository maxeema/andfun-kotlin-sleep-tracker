
package com.example.android.trackmysleepquality

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.android.trackmysleepquality.databinding.FragmentDetailsBinding
import com.example.android.trackmysleepquality.ext.hash
import com.example.android.trackmysleepquality.viewmodel.DetailsViewModel
import org.jetbrains.anko.info

class DetailsFragment : BaseFragment() {

    override val model by viewModels<DetailsViewModel> {
        DetailsViewModel.FACTORY(DetailsFragmentArgs.fromBundle(requireArguments()).nightId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        info("$hash onCreateView, $savedInstanceState")

        val binding = FragmentDetailsBinding.inflate(inflater, container, false)

        binding.model = model
        binding.lifecycleOwner = this

        model.onComplete = {
            findNavController().popBackStack()
            model.onComplete = null
        }

        return binding.root
    }

}
