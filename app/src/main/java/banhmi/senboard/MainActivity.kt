package banhmi.senboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.remember
import banhmi.senboard.app.SenAppView
import banhmi.senboard.app.settings.SenBoardPreferences
import banhmi.senboard.ui.theme.SenBoardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val prefs = banhmi.senboard.app.settings.rememberPreferences()
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