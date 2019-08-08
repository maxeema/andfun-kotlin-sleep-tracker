package com.example.android.trackmysleepquality

import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.android.trackmysleepquality.misc.Prefs
import com.example.android.trackmysleepquality.ext.hash
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainActivity : AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        info("$hash onCreate ${savedInstanceState?.run { ", savedInstanceState: $this"} ?: ""} ")
        super.onCreate(savedInstanceState)

        supportActionBar?.elevation = 0f
        setContentView(R.layout.activity_main)

        navHostFrag.findNavController().apply {
            info(" startOn ${graph.startDestination}, cur ${currentDestination?.id}")

            addOnDestinationChangedListener { nc, nd, args ->
                info(" dest changed to ${nd.id}, ${nc.currentDestination?.id}, ${nd.label}, ${nd.navigatorName}")
                title = nd.label
            }

            if (graph.startDestination == currentDestination?.id) navigate(when (Prefs.lastNightActive) {
                true -> BaseFragmentDirections.actionNavigationFragToSleepingFrag(requireNotNull(Prefs.lastNightId))
                else -> BaseFragmentDirections.actionNavigationFragToTrackerFrag()
            })
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

}
