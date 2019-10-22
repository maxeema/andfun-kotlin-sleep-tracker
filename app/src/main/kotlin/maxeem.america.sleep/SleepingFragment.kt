package maxeem.america.sleep

import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.ChangeTransform
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import maxeem.america.sleep.databinding.FragmentSleepingBinding
import maxeem.america.sleep.ext.animFadeIn
import maxeem.america.sleep.ext.hash
import maxeem.america.sleep.ext.invisibleFadeOutDelayed
import maxeem.america.sleep.ext.startIfItAnimatable
import maxeem.america.sleep.misc.Utils
import maxeem.america.sleep.model.SleepingModel
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.info
import java.util.concurrent.TimeUnit.MINUTES
import java.util.concurrent.TimeUnit.SECONDS
import kotlin.math.abs
import kotlin.random.Random

class SleepingFragment : BaseFragment() {

    private  val  args by navArgs<SleepingFragmentArgs>()
    override val model by viewModels<SleepingModel> { SleepingModel.FACTORY(args.nightId) }

    private lateinit var binding : FragmentSleepingBinding

    private val screen by lazy { Screen(SECONDS.toMillis(3), MINUTES.toMillis(10),
                                        activity!!.window, binding.root, binding.sleepingImage) }
    private var firstStart = true

    override fun consumeBackPressed() = true.apply {
        screen.awake()
        requireView().snackbar(R.string.finish_sleep_first)
    }
    override fun handleUserInteracted() {
        screen.awake()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        info("$hash onCreateView, $savedInstanceState")
        firstStart = savedInstanceState == null

        binding = FragmentSleepingBinding.inflate(inflater, container, false)

        binding.model = model
        binding.lifecycleOwner = viewLifecycleOwner

        binding.sleepingImage.drawable.startIfItAnimatable()
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

        return binding.root
    }

    override fun onStart() { super.onStart()
        screen.lock()
        screen.awake()
        if (firstStart) {
            firstStart = false
            binding.tip.visibility = View.VISIBLE
            binding.tip.invisibleFadeOutDelayed(2000)
        }
    }
    override fun onStop() { super.onStop()
        screen.awake(fully = true)
    }

    private class Screen(val dimTimeout: Long, val animateInterval: Long,
                         val window: Window,   val root: ViewGroup, img: ImageView) {
        companion object {
            const val TRANSITION_DURATION = 1_500L
            const val NEXT_TRANSLATION_MIN_FACTOR = .15f
        }

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
                        duration = TRANSITION_DURATION
                        addTransition(ChangeTransform())
                    })
                    var newTransX : Float; var newTransY : Float
                    val curTransX: Float = translationX; val curTransY = translationY
                    do {
                        newTransX = Random.nextInt(-left, root.width - right).toFloat()
                        newTransY = Random.nextInt(-top, root.height - bottom).toFloat()
                    } while (!(abs(newTransX - curTransX) > NEXT_TRANSLATION_MIN_FACTOR.times(root.width)
                            || abs(newTransY - curTransY) > NEXT_TRANSLATION_MIN_FACTOR.times(root.height)))
                    translationX = newTransX
                    translationY = newTransY
                }
            }
            fun dim() = dim(true)
            fun dim(value: Boolean) = Utils.dim(value, img)
        }
    }

}
