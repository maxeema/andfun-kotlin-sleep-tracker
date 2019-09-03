package maxeem.america.sleep

import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.activity_main.*
import maxeem.america.sleep.ext.hash
import maxeem.america.sleep.misc.Prefs
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainActivity : AppCompatActivity(), AnkoLogger {

    override fun onUserInteraction() {
        currentFrag?.handleUserInteracted()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        info("$hash onCreate ${savedInstanceState ?: ""}")
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        build.text = app.packageInfo.versionName.substringAfter('-').toUpperCase()

        navHostFrag.findNavController().apply {
            info(" startOn ${graph.startDestination}, cur ${currentDestination?.id}")
            if (graph.startDestination == currentDestination?.id) when {
                Prefs.run { hasData.get() && lastNightHasToQualified }
                    -> BaseFragmentDirections.actionNavigationFragToQualityFrag(requireNotNull(Prefs.lastNightId))
                else -> BaseFragmentDirections.actionNavigationFragToJournalFrag()
            }.let { navigate(it) }
            addOnDestinationChangedListener { nc, nd, args ->
                println("dest changed, cur: ${nd.id}, ${nd.label}}, start is: ${graph.startDestination}")
            }
        }
    }

    override fun onSupportNavigateUp()
            = NavigationUI.navigateUp(navHostFrag.findNavController(), null)

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
