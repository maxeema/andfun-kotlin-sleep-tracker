package maxeem.america.sleep

import android.os.Bundle
import android.view.*
import androidx.databinding.ObservableBoolean
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.transition.*
import maxeem.america.sleep.adapter.JournalAdapter
import maxeem.america.sleep.data.Night
import maxeem.america.sleep.databinding.FragmentJournalBinding
import maxeem.america.sleep.ext.*
import maxeem.america.sleep.misc.timeMillis
import maxeem.america.sleep.viewmodel.JournalViewModel
import org.jetbrains.anko.alert
import org.jetbrains.anko.info

class JournalFragment : BaseFragment() {

    private companion object {
        private var startKey : Int? = null
    }

    override val model by viewModels<JournalViewModel> { SavedStateViewModelFactory(app, this) }
    private lateinit var binding : FragmentJournalBinding

    private val loaded = ObservableBoolean()
    private val busy = ObservableBoolean()
    private var inserting = false to null as Long?

    private val navigateToSleepEvent = MutableLiveData<Long?>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        info("$hash onCreateView, savedInstanceState: $savedInstanceState")

        binding = FragmentJournalBinding.inflate(inflater, container, false)

        compatActivity()?.setSupportActionBar(binding.toolbar)

        binding.lifecycleOwner = this
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

        return binding.root
    }

    private fun doLoad() {
        val adapter = JournalAdapter()
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager.grid().apply {
            spanSizeLookup = object: GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int) = when {
                    adapter.isVirtualAt(position) -> spanCount
                    else -> 1
                }
            }
        }
        binding.recycler.itemAnimator = DefaultItemAnimator()
        binding.sleep.setOnClickListener {
            if (busy.get()) return@setOnClickListener
            model.doSleep()
        }
        adapter.onItemClick = View.OnClickListener { val night = it.tag as Night
            if (busy.get()) return@OnClickListener
            findNavController().navigate(when (night.isActive()) {
                true -> JournalFragmentDirections.actionJournalFragToSleepingFrag(night.id!!)
                else -> JournalFragmentDirections.actionJournalFragToDetailsFrag(night.id!!)
            })
        }
        adapter.onItemLongClick = View.OnLongClickListener { val night = it.tag as Night
            if (busy.get()) return@OnLongClickListener true
            requireActivity().alert(R.string.confirm_deleting) {
                positiveButton(R.string.yes) {
                    model.deleteItem(night)
                }
                negativeButton(R.string.cancel) { it.dismiss() }
                show()
            }
            true
        }
        model.onComplete = { val newNightId = it as Long
            busy.set(true)
            inserting = true to newNightId
            binding.recycler.scrollToPosition(0)
        }
        model.nights.observe(viewLifecycleOwner) { nights ->
            info((" nights update to -> $nights"))
            nights ?: return@observe
            adapter.submitList(nights)
            busy.set(true)
            compatActivity()?.delayed(500) {
                binding.recycler.itemAnimator?.isRunning {
                    info(" $timeMillis isRunning called on binding.recycler.itemAnimator")
                    busy.set(false)
                    binding.appbar.setExpanded(true)
                    if (inserting.first)
                        navigateToSleepEvent.value = inserting.second
                }
            }
            clear?.isVisible = nights.isNotEmpty()
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
        binding.sleep.apply { scaleX = 0f; scaleY = 0f }
        compatActivity()?.delayed(200, Lifecycle.State.STARTED) {
            TransitionManager.beginDelayedTransition(binding.root, TransitionSet().apply {
                ordering = TransitionSet.ORDERING_SEQUENTIAL
                addTransition(Slide(Gravity.START)) // empty text will disappeared to start
                addTransition(Fade(Fade.IN)) // recycler will appeared with fade in
                addTransition(ChangeTransform()) // sleep fab will appear from nothing
            })
            doLoad()
            binding.sleep.apply { scaleX = 1f; scaleY = 1f }
        }
    }

    private var clear : MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.journal_menu, menu)
        clear = menu.findItem(R.id.clear).apply {
            isVisible = loaded.get() && model.hasNights.value == true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when {
        busy.get() -> false
        item == clear -> requireActivity().alert(R.string.confirm_clearing) {
            positiveButton(R.string.yes) {
                model.clearData(); clear?.isVisible = false
            }
            negativeButton(R.string.cancel) { it.dismiss() }
            show()
        }.let { true }
        else -> super.onOptionsItemSelected(item)
    }

}
