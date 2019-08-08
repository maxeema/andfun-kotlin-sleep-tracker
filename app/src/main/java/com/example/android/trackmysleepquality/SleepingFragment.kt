package com.example.android.trackmysleepquality

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.trackmysleepquality.databinding.FragmentSleepingBinding
import com.example.android.trackmysleepquality.ext.animFadeIn
import com.example.android.trackmysleepquality.ext.hash
import com.example.android.trackmysleepquality.ext.startIfItAnimatable
import com.example.android.trackmysleepquality.ext.stopIfItAnimatable
import com.example.android.trackmysleepquality.misc.Screen
import com.example.android.trackmysleepquality.viewmodel.SleepingViewModel
import kotlinx.android.synthetic.main.fragment_sleeping.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.info

class SleepingFragment : BaseFragment() {

    private val screen by lazy { Screen(2000L, activity?.window, lifecycle) }
    private  val  args by navArgs<SleepingFragmentArgs>()
    override val model by viewModels<SleepingViewModel> { SleepingViewModel.FACTORY(args.nightId) }

    override fun consumeBackPressed() = true.apply {
        screen.awake()
        requireView().snackbar(R.string.finish_sleep_first)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        info("$hash onCreateView, $savedInstanceState")

        val binding = FragmentSleepingBinding.inflate(inflater, container, false)

        binding.model = model
        binding.lifecycleOwner = this

        model.onTap = {
            screen.awake(); binding.tip.animFadeIn()
        }
        model.onComplete = {
            findNavController().navigate(SleepingFragmentDirections.actionSleepingFragToQualityFrag(args.nightId))
            model.onComplete = null
        }

        return binding.root
    }

    override fun onStart() { super.onStart()
        sleepingImage.drawable.startIfItAnimatable()
    }
    override fun onResume() { super.onResume()
        screen.sleep()
    }
    override fun onPause() { super.onPause()
        screen.awake(fully = true)
    }
    override fun onStop() { super.onStop()
        sleepingImage.drawable.stopIfItAnimatable()
    }

}
