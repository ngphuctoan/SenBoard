package banhmi.senboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import banhmi.senboard.app.navigation.SenEntryProviderInstaller
import banhmi.senboard.app.navigation.SenNavigator
import banhmi.senboard.data.preferences.SenPreferencesViewModel
import banhmi.senboard.ui.theme.SenTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SenActivity : ComponentActivity() {
    @Inject
    lateinit var navigator: SenNavigator

    @Inject
    lateinit var entryProviderScopes: Set<@JvmSuppressWildcards SenEntryProviderInstaller>

    // No ViewModelProvider.Factory needed, wow :O
    private val preferencesViewModel: SenPreferencesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SenTheme {
                NavDisplay(
                    backStack = navigator.backStack,
                    onBack = { navigator.goBack() },
                    entryProvider = entryProvider {
                        entryProviderScopes.forEach { builder -> this.builder() }
                    },
                )
            }
        }
    }
}
