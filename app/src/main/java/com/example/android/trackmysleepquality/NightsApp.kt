package com.example.android.trackmysleepquality

import android.app.Application

val app = App.instance

class App : Application() {

    companion object {
        private var initializer : (()-> App)? = null
        val instance by lazy { requireNotNull(initializer).apply{ initializer = null }()  }
    }

    init { initializer = { this } }

}