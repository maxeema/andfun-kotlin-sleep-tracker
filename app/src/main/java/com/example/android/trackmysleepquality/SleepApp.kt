package com.example.android.trackmysleepquality

import android.app.Application

val app = SleepApp.instance

class SleepApp : Application() {

    companion object {
        private var initializer : (()-> SleepApp)? = null
        val instance by lazy { requireNotNull(initializer).apply{ initializer = null }()  }
    }

    init { initializer = { this } }

}