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
import com.example.android.trackmysleepquality.util.hash
import com.example.android.trackmysleepquality.util.prepareDetailedError
import com.example.android.trackmysleepquality.viewmodel.BaseViewModel.MessageEvent.Error
import com.example.android.trackmysleepquality.viewmodel.BaseViewModel.MessageEvent.Info
import com.example.android.trackmysleepquality.viewmodel.QualityViewModel
import com.google.android.material.snackbar.Snackbar
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.alert
import org.jetbrains.anko.info

class QualityFragment : Fragment(), AnkoLogger {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        info("$hash onCreateView")

        val model by viewModels<QualityViewModel>{ QualityViewModel.FACTORY(QualityFragmentArgs.fromBundle(requireArguments()).nightId) }
        val binding = FragmentQualityBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.model = model

        model.completeEvent.observe(viewLifecycleOwner) {
            info("completeEvent observing $it")
            if (it) findNavController().popBackStack()
        }
        model.messageEvent.observe(viewLifecycleOwner) { msg ->
            msg ?: return@observe
            when (msg) {
                is Info -> Snackbar.make(binding.root, msg.msg, Snackbar.LENGTH_SHORT).show()
                is Error ->
                    Snackbar.make(binding.root, msg.msg, Snackbar.LENGTH_LONG).apply {
                        setAction(R.string.details) { requireActivity().alert(prepareDetailedError(msg.msg, msg.err)).show() }
                    }.show()
            }
            model.messageEventConsumed()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        info("$hash onDestroyView")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        info("$hash onCreate")
    }

    override fun onDestroy() {
        super.onDestroy()
        info("$hash onDestroy")
    }

}
