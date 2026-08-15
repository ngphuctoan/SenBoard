package banhmi.senboard.app.settings

import android.graphics.drawable.AdaptiveIconDrawable
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import banhmi.senboard.R
import banhmi.senboard.app.annotations.SenPreviewCommon
import banhmi.senboard.app.navigation.SenEntryProviderInstaller
import banhmi.senboard.app.navigation.SenNavigator
import banhmi.senboard.app.ui.SenColumn
import banhmi.senboard.app.ui.SenScaffold
import banhmi.senboard.app.ui.SenTopBar
import banhmi.senboard.app.ui.SenTopBarBackButton
import banhmi.senboard.app.ui.appIconShape
import banhmi.senboard.app.ui.rememberSenTopBarState
import banhmi.senboard.data.preferences.SenPreferences
import banhmi.senboard.data.preferences.SenPreferencesViewModel
import banhmi.senboard.ui.theme.SenTheme
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

object SenAbout

@Module
@InstallIn(ActivityRetainedComponent::class)
object SenAboutModule {
    @RequiresApi(Build.VERSION_CODES.O)
    @Provides
    @IntoSet
    fun provideEntryProviderInstaller(navigator: SenNavigator): SenEntryProviderInstaller = {
        entry<SenAbout> {
            SenAboutScreen(navigator)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SenAboutScreen(
    navigator: SenNavigator,
    preferencesViewModel: SenPreferencesViewModel = hiltViewModel(),
) {
    val preferences by preferencesViewModel.preferences.collectAsState()

    SenAboutContent(
        onNavigateBack = navigator::goBack,
        easterEggEnabled = preferences.easterEggEnabled,
        onEasterEggEnabledUpdate = preferencesViewModel::updateEasterEggEnabled,
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SenAboutContent(
    onNavigateBack: () -> Unit = {},
    easterEggEnabled: Boolean,
    onEasterEggEnabledUpdate: (Boolean) -> Unit,
) {
    val topAppBarState = rememberSenTopBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(topAppBarState)

    val drawable = AdaptiveIconDrawable(
        LocalResources.current.getDrawable(
            R.drawable.ic_launcher_background,
            LocalContext.current.theme,
        ),
        LocalResources.current.getDrawable(
            R.drawable.ic_launcher_foreground,
            LocalContext.current.theme,
        )
    )

    SenScaffold(
        topBar = {
            SenTopBar(
                title = { Text("Giới thiệu ứng dụng") },
                navigationIcon = { SenTopBarBackButton(onClick = onNavigateBack) },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        SenColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
        ) {
            item {
//                Image(
//                    painter = rememberDrawablePainter(drawable = drawable),
//                    contentDescription = "content description",
//                )
                Box(
                    modifier = Modifier
                        .size(1000.dp)
                        .background(Color.Blue, appIconShape()),
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@SenPreviewCommon
fun SenAboutScreenPreview() {
    var preferences by remember { mutableStateOf(SenPreferences()) }

    SenTheme {
        SenAboutContent(
            easterEggEnabled = preferences.easterEggEnabled,
            onEasterEggEnabledUpdate = { easterEggEnabled ->
                preferences = preferences.copy(
                    easterEggEnabled = easterEggEnabled,
                )
            },
        )
    }
}
