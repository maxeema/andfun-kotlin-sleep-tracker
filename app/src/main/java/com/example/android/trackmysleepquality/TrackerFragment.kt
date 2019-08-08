package com.example.android.trackmysleepquality

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.adapter.TrackerAdapter
import com.example.android.trackmysleepquality.data.Night
import com.example.android.trackmysleepquality.ext.isActive
import com.example.android.trackmysleepquality.databinding.FragmentTrackerBinding
import com.example.android.trackmysleepquality.misc.Prefs
import com.example.android.trackmysleepquality.ext.grid
import com.example.android.trackmysleepquality.ext.hash
import com.example.android.trackmysleepquality.viewmodel.TrackerViewModel
import org.jetbrains.anko.alert
import org.jetbrains.anko.info

class TrackerFragment : BaseFragment() {

    override val model by viewModels<TrackerViewModel> { SavedStateViewModelFactory(this) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        info("$hash onCreateView, savedInstanceState: $savedInstanceState")

        val binding = FragmentTrackerBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.model = model

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
                binding.recycler.scrollToPosition(0)
            }
        })
        model.nights.observe(viewLifecycleOwner) { nights ->
            info((" nights update to -> $nights"))
            adapter.submitList(nights.takeUnless { it.isEmpty() })
            clear?.isVisible = nights.isNotEmpty()
        }
        model.onComplete = {
            findNavController().navigate(TrackerFragmentDirections.actionTrackerFragToSleepingFrag(Prefs.lastNightId!!))
            model.onComplete = null
        }
        return binding.root
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
            isVisible = model.hasNights.value ?: false
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
