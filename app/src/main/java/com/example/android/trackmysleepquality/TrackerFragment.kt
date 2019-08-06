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
import com.example.android.trackmysleepquality.util.Prefs
import com.example.android.trackmysleepquality.util.hash
import com.example.android.trackmysleepquality.util.prepareDetailedError
import com.example.android.trackmysleepquality.viewmodel.BaseViewModel.MessageEvent.Error
import com.example.android.trackmysleepquality.viewmodel.BaseViewModel.MessageEvent.Info
import com.example.android.trackmysleepquality.viewmodel.TrackerViewModel
import com.google.android.material.snackbar.Snackbar
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.alert
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
            findNavController().navigate(TrackerFragmentDirections.actionTrackerFragToDetailsFrag(night.id!!))
        }
        adapter.registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                binding.recycler.scrollToPosition(0)
            }
        })
        binding.recycler.layoutManager.let { it as GridLayoutManager }.apply {
            spanCount = resources.getInteger(R.integer.grid_span_count)
        }
        binding.empty.setText(Prefs.lastNight?.let { R.string.loading_nights } ?: R.string.no_nights_yet)
        model.nights.observe(viewLifecycleOwner) { nights ->
            info((" nights update to -> $nights"))
            adapter.submitList(nights.takeIf { it.isNotEmpty()})
            clear?.isVisible = nights.isNotEmpty()
            if (nights.isEmpty())
                binding.empty.setText(R.string.no_nights_yet)
        }
        model.qualifyEvent.observe(viewLifecycleOwner) { night ->
            night ?: return@observe
            binding.recycler.scrollToPosition(0)
            findNavController().navigate(TrackerFragmentDirections.actionTrackerFragToQualityFrag(night.id!!))
            model.qualifyEventConsumed()
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
