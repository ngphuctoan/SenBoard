package banhmi.senboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import banhmi.senboard.app.SenAppView
import banhmi.senboard.app.settings.rememberPreferences
import banhmi.senboard.ui.theme.SenBoardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Read preference value reactively using rememberPreferences() helper
            val prefs = rememberPreferences()
            val darkTheme = when (prefs.themeMode) {
                "light" -> false
                "dark" -> true
                else -> isSystemInDarkTheme()
            }
            SenBoardTheme(darkTheme = darkTheme) {
                SenAppView()
            }
        }
    }
}