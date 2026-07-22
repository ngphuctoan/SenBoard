package banhmi.senboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import banhmi.senboard.app.SenAppView
import banhmi.senboard.shared.settings.SenSettingsViewModel
import banhmi.senboard.ui.theme.SenBoardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: SenSettingsViewModel = viewModel(factory = SenSettingsViewModel.Factory)
            val appearanceState by viewModel.appearanceState.collectAsStateWithLifecycle()
            SenBoardTheme(oled = appearanceState.oledThemeEnabled) { SenAppView() }
        }
    }
}
