package com.example.android.trackmysleepquality.util

import androidx.core.content.edit
import com.example.android.trackmysleepquality.app
import org.jetbrains.anko.defaultSharedPreferences

object Prefs {

    private const val LAST_NIGHT_ID = "last_night_id"

    private val prefs = app.defaultSharedPreferences

    var lastNight
        get() = if (prefs.contains(LAST_NIGHT_ID)) prefs.getLong(LAST_NIGHT_ID, -1) else null
        set(value) = prefs.edit(true) { value?.apply { putLong(LAST_NIGHT_ID, value) ?: remove(LAST_NIGHT_ID) } }

}