package com.example.android.trackmysleepquality

import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.core.os.postDelayed
import androidx.databinding.ObservableBoolean
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.*
import com.example.android.trackmysleepquality.adapter.TrackerAdapter
import com.example.android.trackmysleepquality.data.Night
import com.example.android.trackmysleepquality.databinding.FragmentTrackerBinding
import com.example.android.trackmysleepquality.ext.grid
import com.example.android.trackmysleepquality.ext.hash
import com.example.android.trackmysleepquality.ext.isActive
import com.example.android.trackmysleepquality.misc.Prefs
import com.example.android.trackmysleepquality.misc.app
import com.example.android.trackmysleepquality.viewmodel.TrackerViewModel
import org.jetbrains.anko.alert
import org.jetbrains.anko.info

class TrackerFragment : BaseFragment() {

    private companion object {
        private var startUpKey = -1
    }

    override val model by viewModels<TrackerViewModel> { SavedStateViewModelFactory(app, this) }
    private lateinit var binding : FragmentTrackerBinding
    private lateinit var loaded : ObservableBoolean

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        info("$hash onCreateView, savedInstanceState: $savedInstanceState")

        loaded = ObservableBoolean(false)
        binding = FragmentTrackerBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.model = model
        binding.loaded = loaded

        info(" startUpKey: $startUpKey, cur key: ${context!!.hash}, isAdded: $isAdded")
        if (startUpKey == context!!.hash)
            doLoad()

        return binding.root
    }

    private fun doLoad() {
        val adapter = TrackerAdapter()
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager.grid().spanCount = resources.getInteger(R.integer.grid_span_count)

        adapter.onItemClick = View.OnClickListener { val night = it.tag as Night
            findNavController().navigate(when (night.isActive()) {
                true -> TrackerFragmentDirections.actionTrackerFragToSleepingFrag(night.id!!)
                else -> TrackerFragmentDirections.actionTrackerFragToDetailsFrag(night.id!!)
            })
        }
        adapter.registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                info(" onItemRangeInserted: start: $positionStart, count: $itemCount}")
                if (loaded.get())
                    binding.recycler.scrollToPosition(0)
            }
        })
        model.onComplete = {
            findNavController().navigate(TrackerFragmentDirections.actionTrackerFragToSleepingFrag(Prefs.lastNightId!!))
            model.onComplete = null
        }
        model.nights.observe(viewLifecycleOwner) { nights ->
            info((" nights update to -> $nights"))
            nights ?: return@observe
            adapter.submitList(nights.takeUnless { it.isEmpty() })
            clear?.isVisible = nights.isNotEmpty()
        }

        loaded.set(true)
        info(" loaded: ${loaded.get()}")
    }

    override fun onResume() {
        super.onResume()
        preLoad()
    }

    private fun preLoad() {
        if (loaded.get()) return

        startUpKey = requireActivity().hash
        info(" load on key: $startUpKey")

        binding.sleep.visibility = View.INVISIBLE
        Handler().postDelayed(200) {
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                TransitionManager.beginDelayedTransition(binding.root, TransitionSet().apply {
                    ordering = TransitionSet.ORDERING_SEQUENTIAL
                    addTransition(Slide(Gravity.START))
                    addTransition(Fade(Fade.IN))
                    doLoad()
                }.addListener(object: TransitionListenerAdapter() {
                    override fun onTransitionEnd(transition: Transition) {
                        info(" end transition $transition")
                        TransitionManager.beginDelayedTransition(binding.root, TransitionSet().apply {
                            addTransition(Explode()) // addTransition(Slide(Gravity.BOTTOM))
                        })
                        binding.sleep.visibility = View.VISIBLE
                    }
                }))
            }
        }
    }

    private var clear : MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.tracker_menu, menu)
        clear = menu.findItem(R.id.clear).apply {
            isVisible = loaded.get() && model.hasNights.value == true ?: false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when(item) {
        clear -> requireActivity().alert(R.string.confirm_clearing) {
            positiveButton(R.string.yes) {
                model.onClearData(); clear?.isVisible = false
            }
            negativeButton(R.string.cancel) { it.dismiss() }
            show()
        }.let { true }
        else -> super.onOptionsItemSelected(item)
    }

}
