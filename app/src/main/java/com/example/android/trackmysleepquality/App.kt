package com.example.android.trackmysleepquality

import android.app.Application

val app = NightsApp.instance

class NightsApp : Application() {

    companion object {
        private var initializer : (()-> NightsApp)? = null
        val instance by lazy { requireNotNull(initializer).apply{ initializer = null }()  }
    }

    init { initializer = { this } }

}