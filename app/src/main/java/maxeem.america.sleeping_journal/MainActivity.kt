package maxeem.america.sleeping_journal

import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.activity_main.*
import maxeem.america.sleeping_journal.ext.hash
import maxeem.america.sleeping_journal.misc.Prefs
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainActivity : AppCompatActivity(), AnkoLogger {

    override fun onUserInteraction() {
        currentFrag?.handleUserInteracted()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        info("$hash onCreate ${savedInstanceState?.run { ", savedInstanceState: $this"} ?: ""} ")
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        navHostFrag.findNavController().apply {
            info(" startOn ${graph.startDestination}, cur ${currentDestination?.id}")
//            addOnDestinationChangedListener { nc, nd, args ->
//                info(" dest changed to ${nd.id}, ${nc.currentDestination?.id}, ${nd.label}, ${nd.navigatorName}")
//                title = nd.label
//            }
            if (graph.startDestination == currentDestination?.id) when {
                Prefs.lastNightActive -> BaseFragmentDirections.actionNavigationFragToSleepingFrag(requireNotNull(Prefs.lastNightId))
                else -> when {
                    Prefs.run { hasData.get() && !lastNightQualified } -> BaseFragmentDirections.actionNavigationFragToQualityFrag(requireNotNull(Prefs.lastNightId))
                    else -> BaseFragmentDirections.actionNavigationFragToJournalFrag()
                }
            }.also { navigate(it) }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (currentFrag?.consumeBackPressed() == true)
                return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private val currentFrag get() =
        runCatching { (navHostFrag as NavHostFragment).childFragmentManager.fragments.first() as BaseFragment }.getOrNull()

    override fun onDestroy() { super.onDestroy()
        info("$hash onDestroy")
    }

}
