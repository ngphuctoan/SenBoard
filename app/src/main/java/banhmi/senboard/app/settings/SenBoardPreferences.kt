package banhmi.senboard.app.settings

import android.content.Context
import android.content.SharedPreferences

class SenBoardPreferences(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("senboard_prefs", Context.MODE_PRIVATE)

    var typingMode: String
        get() = prefs.getString("typing_mode", "telex") ?: "telex"
        set(value) = prefs.edit().putString("typing_mode", value).apply()

    var autoCapitalize: Boolean
        get() = prefs.getBoolean("auto_capitalize", true)
        set(value) = prefs.edit().putBoolean("auto_capitalize", value).apply()

    var doubleSpacePeriod: Boolean
        get() = prefs.getBoolean("double_space_period", true)
        set(value) = prefs.edit().putBoolean("double_space_period", value).apply()
}
