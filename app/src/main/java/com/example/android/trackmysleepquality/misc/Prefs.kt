package com.example.android.trackmysleepquality.util

import androidx.core.content.edit
import androidx.databinding.ObservableBoolean
import com.example.android.trackmysleepquality.app
import org.jetbrains.anko.defaultSharedPreferences

object Prefs {

    private const val LAST_NIGHT_ID = "last_night_id"
    private const val LAST_NIGHT_ACTIVE = "last_night_active"

    private val prefs = app.defaultSharedPreferences

    val hasData = ObservableBoolean(lastNightId != null)

    var lastNightId
        get() = if (prefs.contains(LAST_NIGHT_ID)) prefs.getLong(LAST_NIGHT_ID, -1) else null
        set(value) {
            hasData.set(value != null)
            prefs.edit(true) { value?.apply { putLong(LAST_NIGHT_ID, value) } ?: clear() }
        }
    var lastNightActive
        get() = prefs.getBoolean(LAST_NIGHT_ACTIVE, false)
        set(value) = prefs.edit(commit = true) { putBoolean(LAST_NIGHT_ACTIVE, value) }

}