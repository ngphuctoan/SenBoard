package banhmi.senboard.shared.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import banhmi.senboard.app.settings.rememberPreferences

@Composable
fun isAppInDarkTheme(): Boolean {
    val preferences = rememberPreferences()
    return when (preferences.themeMode) {
        "light" -> false
        "dark" -> true
        else -> isSystemInDarkTheme()
    }
}
