package maxeem.america.sleep

import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.ImageView
import androidx.databinding.ObservableBoolean
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.*
import maxeem.america.sleep.databinding.FragmentSleepingBinding
import maxeem.america.sleep.ext.*
import maxeem.america.sleep.misc.Utils
import maxeem.america.sleep.misc.delayed
import maxeem.america.sleep.viewmodel.SleepingViewModel
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.dip
import org.jetbrains.anko.info
import java.util.concurrent.TimeUnit.MINUTES
import java.util.concurrent.TimeUnit.SECONDS

class SleepingFragment : BaseFragment() {

    private  val  args by navArgs<SleepingFragmentArgs>()
    override val model by viewModels<SleepingViewModel> { SleepingViewModel.FACTORY(args.nightId) }

    private lateinit var binding : FragmentSleepingBinding
    private val loaded = ObservableBoolean()

    private val screen by lazy { Screen(SECONDS.toMillis(3), MINUTES.toMillis(10),
                                        activity!!.window, binding.root, binding.sleepingImage) }

    override fun consumeBackPressed() = true.apply {
        screen.awake()
        requireView().snackbar(R.string.finish_sleep_first)
    }
    override fun handleUserInteracted() {
        screen.awake()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        info("$hash onCreateView, $savedInstanceState")

        binding = FragmentSleepingBinding.inflate(inflater, container, false)

        binding.model = model
        binding.lifecycleOwner = this

        return binding.root
    }

    private fun doLoad() {
        binding.sleepingImage.setOnClickListener {
            binding.tip.visibility = View.VISIBLE
            binding.tip.animFadeIn()
            binding.tip.invisibleFadeOutDelayed(1000)
        }
        model.onComplete = {
            screen.unlock()
            findNavController().navigate(SleepingFragmentDirections.actionSleepingFragToQualityFrag(args.nightId))
            model.onComplete = null
        }
        loaded.set(true)
    }

    private fun preLoad() {
        compatActivity()?.delayed(200) {
            if (lifecycle.currentState < Lifecycle.State.STARTED) return@delayed
            TransitionManager.beginDelayedTransition(binding.root, TransitionSet().apply {
                addTransition(Slide(Gravity.BOTTOM))
            }.addListener(object: TransitionListenerAdapter() {
                override fun onTransitionEnd(transition: Transition) {
                    compatActivity()?.delayed(200) {
                        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED))
                            TransitionManager.beginDelayedTransition(binding.root, TransitionSet().apply {
                                addTransition(Explode())
                            })
                        binding.sleepingImage.visibility = View.VISIBLE
                        binding.sleepingImage.drawable.startIfItAnimatable()
                        binding.tip.invisibleFadeOutDelayed(2000)
                        screen.awake()
                    }
                }
            }))
            doLoad()
            binding.tip.visibility = View.VISIBLE
        }
    }

    override fun onStart() { super.onStart()
        screen.lock()
        if (!loaded.get()) {
            preLoad()
        } else {
            screen.awake()
        }
    }
    override fun onStop() { super.onStop()
        screen.awake(fully = true)
    }

    private class Screen(val dimTimeout: Long, val animateInterval: Long,
                         val window: Window, val root: ViewGroup, img: ImageView) {

        private val h by lazy { Handler() }
        private val saver = Saver(root, img)

        private var viewVisibilityBackup : Int? = null
        private var windowFlagsBackup    : Int? = null

        fun lock() {
            if (null == viewVisibilityBackup)
                viewVisibilityBackup = root.systemUiVisibility
            root.systemUiVisibility =
                   View.SYSTEM_UI_FLAG_LOW_PROFILE or View.SYSTEM_UI_FLAG_IMMERSIVE or
                   View.SYSTEM_UI_FLAG_FULLSCREEN  or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                   View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

            val winParams = window.attributes
            if (null == windowFlagsBackup)
                windowFlagsBackup = winParams.flags
            winParams.flags = winParams.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN
            window.attributes = winParams
        }

        fun unlock() {
            viewVisibilityBackup?.also {
                root.systemUiVisibility = it
            }
            windowFlagsBackup?.also {
                val winAttrs = window.attributes
                winAttrs.flags = it
                window.attributes = winAttrs
            }
        }

        fun awake(fully: Boolean = false) {
            h.removeCallbacksAndMessages(null)
            if (fully) return
            animate()
        }
        private fun animate() {
            saver.dim(false)
            h.postDelayed(saver::dim, dimTimeout)
            h.postDelayed(saver::animate, dimTimeout/2)
            h.postDelayed(::animate, animateInterval)
        }

        private class Saver(val root: ViewGroup, val img: ImageView) {
            fun animate() {
                img.apply {
                    TransitionManager.beginDelayedTransition(root, TransitionSet().apply {
                        addTransition(ChangeTransform())
                    })
                    translationY = context!!.dip(kotlin.random.Random.nextInt(-50, 50)).toFloat()
                    translationX = context!!.dip(kotlin.random.Random.nextInt(-50, 50)).toFloat()
                }
            }
            fun dim() = dim(true)
            fun dim(value: Boolean) = Utils.dim(value, img)
        }
    }


}
