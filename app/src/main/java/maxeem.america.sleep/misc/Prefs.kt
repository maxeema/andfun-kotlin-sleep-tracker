package maxeem.america.sleep.misc

import androidx.core.content.edit
import androidx.databinding.ObservableBoolean
import maxeem.america.sleep.app
import org.jetbrains.anko.defaultSharedPreferences

object Prefs {

    private const val LAST_NIGHT_ID = "last_night_id"
    private const val LAST_NIGHT_WAS_FINISHED = "last_night_was_finished"
    private const val LAST_NIGHT_HAS_TO_QUALIFIED = "last_night_has_to_qualified"

    private val prefs = app.defaultSharedPreferences

    val hasData = ObservableBoolean(lastNightId != null)

    var lastNightId
        get() = if (prefs.contains(LAST_NIGHT_ID)) prefs.getLong(LAST_NIGHT_ID, -1) else null
        set(value) {
            hasData.set(value != null)
            prefs.edit { value?.apply { putLong(LAST_NIGHT_ID, value) } ?: clear() }
        }

    var lastNightWasFinished
        get() = prefs.getBoolean(LAST_NIGHT_WAS_FINISHED, false)
        set(value) = prefs.edit { putBoolean(LAST_NIGHT_WAS_FINISHED, value) }

    var lastNightHasToQualified
        get() = prefs.getBoolean(LAST_NIGHT_HAS_TO_QUALIFIED, false)
        set(value) = prefs.edit { putBoolean(LAST_NIGHT_HAS_TO_QUALIFIED, value) }

}
