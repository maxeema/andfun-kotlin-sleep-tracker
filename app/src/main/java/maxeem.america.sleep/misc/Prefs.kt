package maxeem.america.sleep.misc

import androidx.core.content.edit
import androidx.databinding.ObservableBoolean
import org.jetbrains.anko.defaultSharedPreferences

object Prefs {

    private const val LAST_NIGHT_ID = "last_night_id"
    private const val LAST_NIGHT_ACTIVE = "last_night_active"
    private const val LAST_NIGHT_QUALIFIED = "last_night_qualified"

    private val prefs = app.defaultSharedPreferences

    @JvmStatic
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

    var lastNightQualified
        get() = prefs.getBoolean(LAST_NIGHT_QUALIFIED, false)
        set(value) = prefs.edit(commit = true) { putBoolean(LAST_NIGHT_QUALIFIED, value) }

}
