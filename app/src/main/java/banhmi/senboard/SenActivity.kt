package banhmi.senboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import banhmi.senboard.app.navigation.SenEntryProviderInstaller
import banhmi.senboard.app.navigation.SenNavigator
import banhmi.senboard.app.ui.LocalAppIcon
import banhmi.senboard.ui.theme.SenTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SenActivity : ComponentActivity() {
    @Inject
    lateinit var navigator: SenNavigator

    @Inject
    lateinit var entryProviderScopes: Set<@JvmSuppressWildcards SenEntryProviderInstaller>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val resources = LocalResources.current

            val appIcon = resources.getDrawable(R.mipmap.ic_launcher, context.theme)

            SenTheme {
                CompositionLocalProvider(LocalAppIcon provides appIcon) {
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
}
