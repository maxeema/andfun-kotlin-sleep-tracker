package com.example.android.trackmysleepquality

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.elevation = 0f
        setContentView(R.layout.activity_main)
        findNavController(R.id.nav_host_fragment).addOnDestinationChangedListener { ctrl, dst, args ->
            title = dst.label
        }
    }

}
