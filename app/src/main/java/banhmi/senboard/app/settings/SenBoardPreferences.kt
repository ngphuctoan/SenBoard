package banhmi.senboard.app.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

class SenBoardPreferences(context: Context) {

    companion object {
        private const val PREFS_NAME = "senboard_settings"

        const val KEY_TYPING_MODE = "typing_mode"
        const val KEY_AUTO_CAPITALIZE = "auto_capitalize"
        const val KEY_DOUBLE_SPACE_PERIOD = "double_space_period"
        const val KEY_SHOW_SUGGESTIONS = "show_suggestions"

        const val KEY_THEME_MODE = "theme_mode"
        const val KEY_KEYBOARD_HEIGHT = "keyboard_height"
        const val KEY_SHOW_NUMBER_ROW = "show_number_row"
        const val KEY_SHOW_KEY_BORDERS = "show_key_borders"

        const val KEY_HAPTIC_ENABLED = "haptic_enabled"
        const val KEY_HAPTIC_INTENSITY = "haptic_intensity"
        const val KEY_SOUND_ENABLED = "sound_enabled"
        const val KEY_SOUND_VOLUME = "sound_volume"

        const val DEFAULT_TYPING_MODE = "telex"
        const val DEFAULT_THEME_MODE = "system"
        const val DEFAULT_KEYBOARD_HEIGHT = 400
        const val DEFAULT_HAPTIC_INTENSITY = 50
        const val DEFAULT_SOUND_VOLUME = 50
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var typingMode: String
        get() = prefs.getString(KEY_TYPING_MODE, DEFAULT_TYPING_MODE) ?: DEFAULT_TYPING_MODE
        set(value) = prefs.edit { putString(KEY_TYPING_MODE, value) }

    var autoCapitalize: Boolean
        get() = prefs.getBoolean(KEY_AUTO_CAPITALIZE, true)
        set(value) = prefs.edit { putBoolean(KEY_AUTO_CAPITALIZE, value) }

    var doubleSpacePeriod: Boolean
        get() = prefs.getBoolean(KEY_DOUBLE_SPACE_PERIOD, true)
        set(value) = prefs.edit { putBoolean(KEY_DOUBLE_SPACE_PERIOD, value) }

    var showSuggestions: Boolean
        get() = prefs.getBoolean(KEY_SHOW_SUGGESTIONS, true)
        set(value) = prefs.edit { putBoolean(KEY_SHOW_SUGGESTIONS, value) }

    var themeMode: String
        get() = prefs.getString(KEY_THEME_MODE, DEFAULT_THEME_MODE) ?: DEFAULT_THEME_MODE
        set(value) = prefs.edit { putString(KEY_THEME_MODE, value) }

    var keyboardHeight: Int
        get() = prefs.getInt(KEY_KEYBOARD_HEIGHT, DEFAULT_KEYBOARD_HEIGHT)
        set(value) = prefs.edit { putInt(KEY_KEYBOARD_HEIGHT, value) }

    var showNumberRow: Boolean
        get() = prefs.getBoolean(KEY_SHOW_NUMBER_ROW, false)
        set(value) = prefs.edit { putBoolean(KEY_SHOW_NUMBER_ROW, value) }

    var showKeyBorders: Boolean
        get() = prefs.getBoolean(KEY_SHOW_KEY_BORDERS, true)
        set(value) = prefs.edit { putBoolean(KEY_SHOW_KEY_BORDERS, value) }

    var hapticEnabled: Boolean
        get() = prefs.getBoolean(KEY_HAPTIC_ENABLED, true)
        set(value) = prefs.edit { putBoolean(KEY_HAPTIC_ENABLED, value) }

    var hapticIntensity: Int
        get() = prefs.getInt(KEY_HAPTIC_INTENSITY, DEFAULT_HAPTIC_INTENSITY)
        set(value) = prefs.edit { putInt(KEY_HAPTIC_INTENSITY, value) }

    var soundEnabled: Boolean
        get() = prefs.getBoolean(KEY_SOUND_ENABLED, false)
        set(value) = prefs.edit { putBoolean(KEY_SOUND_ENABLED, value) }

    var soundVolume: Int
        get() = prefs.getInt(KEY_SOUND_VOLUME, DEFAULT_SOUND_VOLUME)
        set(value) = prefs.edit { putInt(KEY_SOUND_VOLUME, value) }

    fun resetAll() {
        prefs.edit { clear() }
    }
}

@Composable
fun rememberPreferences(): SenBoardPreferences {
    val context = androidx.compose.ui.platform.LocalContext.current
    var updateTrigger by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(0) }
    val prefs = androidx.compose.runtime.remember(updateTrigger) { SenBoardPreferences(context) }

    androidx.compose.runtime.DisposableEffect(context) {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
            updateTrigger++
        }
        val rawPrefs = context.getSharedPreferences("senboard_settings", Context.MODE_PRIVATE)
        rawPrefs.registerOnSharedPreferenceChangeListener(listener)
        onDispose {
            rawPrefs.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }
    return prefs
}
