package com.example.android.trackmysleepquality

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.os.postDelayed
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionManager
import com.example.android.trackmysleepquality.databinding.FragmentSleepingBinding
import com.example.android.trackmysleepquality.ext.*
import com.example.android.trackmysleepquality.viewmodel.SleepingViewModel
import kotlinx.android.synthetic.main.fragment_sleeping.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.info

class SleepingFragment : BaseFragment() {

    private val screen by lazy { Screen(2_000L, activity?.window, lifecycle) }
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
screen.h.postDelayed(2000) {
    val l = binding.root
    TransitionManager.beginDelayedTransition(l)
    ConstraintSet().apply {
        clone(l)
        setVisibility(binding.sleepingPeriod.id, View.INVISIBLE)
        setVisibility(binding.sleepingImage.id, View.VISIBLE)
        setVisibility(binding.tip.id, View.INVISIBLE)
        applyTo(l)
    }
}
        model.onComplete = {
            findNavController().navigate(SleepingFragmentDirections.actionSleepingFragToQualityFrag(args.nightId))
            model.onComplete = null
        }

        return binding.root
    }

    override fun onStart() { super.onStart()
        sleepingImage.drawable.startIfItAnimatable()
        activity!!.compat().supportActionBar?.setBackgroundDrawable(android.R.color.transparent.asColorDrawable())
    }
    override fun onResume() { super.onResume()
        screen.sleep()
    }
    override fun onPause() { super.onPause()
        screen.awake(fully = true)
        info(" isRemoving onPause: $isRemoving")
        if (isRemoving)
            activity!!.compat().supportActionBar?.setBackgroundDrawable(R.color.colorPrimary.asColorDrawable())
    }
    override fun onStop() { super.onStop()
        sleepingImage.drawable.stopIfItAnimatable()
    }

    private class Screen(val sleepTimeout: Long, val window: Window?, val lifecycle: Lifecycle) {

        val h by lazy { Handler() }
        val flags = window?.decorView?.systemUiVisibility

        fun sleep() {
            window?.decorView?.apply {
                if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED))
                    systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE
            }
        }
        fun awake(fully: Boolean = false) {
            window?.decorView?.apply {
                if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                    systemUiVisibility = flags!!
                    h.removeCallbacksAndMessages(null)
                    if (!fully) h.postDelayed(::sleep, sleepTimeout)
                }
            }
        }

    }

}
