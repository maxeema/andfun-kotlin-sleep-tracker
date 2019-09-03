package maxeem.america.sleep

import android.os.Bundle
import android.view.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.ObservableBoolean
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.transition.*
import com.google.android.material.appbar.AppBarLayout
import maxeem.america.sleep.adapter.JournalAdapter
import maxeem.america.sleep.adapter.JournalAdapterDecoration
import maxeem.america.sleep.adapter.hasOnlyOneRealItem
import maxeem.america.sleep.adapter.isVirtualAt
import maxeem.america.sleep.data.Night
import maxeem.america.sleep.databinding.FragmentJournalBinding
import maxeem.america.sleep.ext.delayed
import maxeem.america.sleep.ext.grid
import maxeem.america.sleep.ext.hash
import maxeem.america.sleep.ext.materialAlert
import maxeem.america.sleep.misc.timeMillis
import maxeem.america.sleep.model.JournalRealModel
import org.jetbrains.anko.contentView
import org.jetbrains.anko.info

class JournalFragment : BaseFragment() {

    private companion object {
        private var startKey : Int? = null
    }

    override val model by viewModels<JournalRealModel>()
    private lateinit var binding : FragmentJournalBinding

    private val busy   = ObservableBoolean()
    private val loaded = ObservableBoolean()

    private var inserting = false to null as Long?

    private val navigateToSleepEvent = MutableLiveData<Long?>()

    private var clear : MenuItem? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        info("$hash onCreateView, savedInstanceState: $savedInstanceState")

        binding = FragmentJournalBinding.inflate(inflater, container, false)
        binding.toolbar.menu

        binding.lifecycleOwner = viewLifecycleOwner
        binding.model = model
        binding.loaded = loaded
        binding.busy = busy

        info(" startKey: $startKey, cur key: ${context?.hash}, savedInstanceState: $savedInstanceState")
        if (startKey == context?.hash || (savedInstanceState != null && startKey != null))
            doLoad()

        navigateToSleepEvent.observe(viewLifecycleOwner) { nightId ->
            nightId ?: return@observe
            navigateToSleepEvent.removeObservers(viewLifecycleOwner)
            if (activity?.isFinishing != true)
                findNavController().navigate(JournalFragmentDirections.actionJournalFragToSleepingFrag(nightId))
        }
        binding.appbar.addOnLayoutChangeListener { v, _,_,_,_,_,_,_,_ ->
            (v.layoutParams as CoordinatorLayout.LayoutParams).behavior?.let {
                if (it is AppBarLayout.Behavior) it.setDragCallback(appbarCantDrag)
            }
        }
        binding.toolbar.apply {
            clear = menu.findItem(R.id.clearMenuItem).apply {
                isEnabled = loaded.get() && model.hasNights.value == true
            }
            setOnMenuItemClickListener { when {
                busy.get() -> false
                it == clear -> materialAlert(R.string.confirm_clearing) {
                    setPositiveButton(R.string.yes) { d, w ->
                        model.clearData(); clear?.isEnabled = false
                    }
                    setNegativeButton(R.string.cancel) { d, w -> }
                }.let { true }
                it.itemId == R.id.aboutFragment -> findNavController().navigate(JournalFragmentDirections.toAbout()).let { true }
                else -> false
            }}
        }
        return binding.root
    }

    private fun doLoad() {
        val adapter = JournalAdapter().apply {
            onItemClick = View.OnClickListener { val night = it.tag as Night
                if (busy.get()) return@OnClickListener
                findNavController().navigate(JournalFragmentDirections.actionJournalFragToDetailsFrag(night.id!!))
            }
            onItemLongClick = View.OnLongClickListener { val night = it.tag as Night
                if (busy.get()) return@OnLongClickListener true
                materialAlert(R.string.confirm_deleting) {
                    setPositiveButton(R.string.yes) { _, _ -> model.deleteItem(night) }
                    setNegativeButton(R.string.cancel) { _, _ -> }
                }
                true
            }
        }
        binding.recycler.apply {
            this.adapter = adapter
            layoutManager.grid().apply {
                spanSizeLookup = object: GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int) = when {
                        adapter.isVirtualAt(position) -> spanCount
                        adapter.hasOnlyOneRealItem -> spanCount
                        else -> 1
                    }
                }
            }
            addItemDecoration(JournalAdapterDecoration(R.color.face))
            itemAnimator = DefaultItemAnimator()
        }
        binding.fab.setOnClickListener {
            if (busy.get()) return@setOnClickListener
            model.doSleep()
        }
        model.onComplete = { val newNightId = it as Long
            busy.set(true)
            inserting = true to newNightId
            binding.recycler.scrollToPosition(0)
        }
        model.nights.observe(viewLifecycleOwner) { nights ->
            info(" nights update to -> $nights")
            nights ?: return@observe

            adapter.submitData(nights)
            busy.set(true)
            viewLifecycleOwner.delayed(500) {
                binding.recycler.itemAnimator?.isRunning {
                    if (lifecycle.currentState < Lifecycle.State.CREATED)
                        return@isRunning
                    info(" $timeMillis isRunning called on binding.recycler.itemAnimator")
                    busy.set(false)
                    val (isInserting, nightId) = inserting
                    if (isInserting)
                        navigateToSleepEvent.value = nightId
                    else
                        binding.appbar.setExpanded(true, false)
                }
            }
            clear?.isEnabled = nights.isNotEmpty()
        }

        startKey = context?.hash
        loaded.set(true)
        busy.set(false)

        info(" loaded: ${loaded.get()}, new startKey: $startKey")
    }

    override fun onStart() { super.onStart()
        if (!loaded.get())
            preLoad()
    }

    private fun preLoad() {
        info(" preLoad")
        binding.fab.apply { scaleX = 0f; scaleY = 0f }
        viewLifecycleOwner.delayed(200, Lifecycle.State.STARTED) {
            TransitionManager.beginDelayedTransition(activity!!.contentView as ViewGroup, TransitionSet().apply {
                ordering = TransitionSet.ORDERING_SEQUENTIAL
                addTransition(Slide(Gravity.START)) // empty text will disappeared to start
                addTransition(Fade(Fade.IN))        // recycler will appeared with fade in
                addTransition(ChangeTransform())    // fab will appear from zero
            })
            doLoad()
            binding.fab.apply { scaleX = 1f; scaleY = 1f }
            activity!!.invalidateOptionsMenu()
        }
    }

    // a little optimization of the UI when user can swipe appbar layout out via its dragging - looks not good
    private val appbarCantDrag = object: AppBarLayout.Behavior.DragCallback() {
        override fun canDrag(appBarLayout: AppBarLayout) = false
    }

}
