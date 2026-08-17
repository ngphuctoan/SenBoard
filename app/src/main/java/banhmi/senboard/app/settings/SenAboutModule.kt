package banhmi.senboard.app.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import banhmi.senboard.BuildConfig
import banhmi.senboard.R
import banhmi.senboard.app.annotations.SenPreviewCommon
import banhmi.senboard.app.icon.LocalSenAppIcon
import banhmi.senboard.app.icon.SenAppIconResult
import banhmi.senboard.app.icon.senAppIconShape
import banhmi.senboard.app.navigation.SenEntryProviderInstaller
import banhmi.senboard.app.navigation.SenNavigator
import banhmi.senboard.app.ui.SenColumn
import banhmi.senboard.app.ui.SenMenu
import banhmi.senboard.app.ui.SenMenuDefaults
import banhmi.senboard.app.ui.SenScaffold
import banhmi.senboard.app.ui.SenTopBar
import banhmi.senboard.app.ui.SenTopBarBackButton
import banhmi.senboard.app.ui.rememberSenTopBarState
import banhmi.senboard.app.ui.segmentedPadding
import banhmi.senboard.data.preferences.SenPreferences
import banhmi.senboard.data.preferences.SenPreferencesViewModel
import banhmi.senboard.shared.utils.outOf
import banhmi.senboard.ui.theme.SenTheme
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

object SenAbout

@Module
@InstallIn(ActivityRetainedComponent::class)
object SenAboutModule {
    @Provides
    @IntoSet
    fun provideEntryProviderInstaller(navigator: SenNavigator): SenEntryProviderInstaller = {
        entry<SenAbout> {
            SenAboutScreen(navigator)
        }
    }
}

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

@Composable
fun SenAboutContent(
    onNavigateBack: () -> Unit = {},
    easterEggEnabled: Boolean,
    onEasterEggEnabledUpdate: (Boolean) -> Unit,
) {
    val topAppBarState = rememberSenTopBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(topAppBarState)

    val appIcon = LocalSenAppIcon.current

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
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 24.dp, start = 16.dp, end = 16.dp),
                ) {
                    if (appIcon is SenAppIconResult.Success) {
                        // Can't use @Composable function in graphicsLayer, so define the shape here instead
                        val appIconShape = senAppIconShape()
                        // Not sure if this is the correct shadow/elevation
                        val elevation = with(LocalDensity.current) { 4.dp.toPx() }

                        Image(
                            painter = rememberDrawablePainter(appIcon.drawable),
                            contentDescription = "Biểu tượng của ứng dụng",
                            modifier = Modifier
                                .size(72.dp)
                                .graphicsLayer {
                                    shape = appIconShape
                                    shadowElevation = elevation
                                },
                        )
                    }
                    Text("SenBoard", style = MaterialTheme.typography.titleLarge)
                }
            }

            item {
                SenMenu(
                    shapes = SenMenuDefaults.segmentedShapes(0 outOf 2),
                    supportingContent = { Text(BuildConfig.VERSION_NAME) },
                    modifier = Modifier.segmentedPadding(),
                ) {
                    Text("Phiên bản ứng dụng")
                }
            }

            item {
                SenMenu(
                    shapes = SenMenuDefaults.segmentedShapes(1 outOf 2),
                    supportingContent = { Text("Apache-2.0") },
                ) {
                    Text("Giấy phép")
                }
            }
        }
    }
}

@Composable
@SenPreviewCommon
fun SenAboutScreenPreview() {
    val context = LocalContext.current
    val resources = LocalResources.current

    val drawable = resources.getDrawable(R.mipmap.ic_launcher, context.theme)

    var preferences by remember { mutableStateOf(SenPreferences()) }

    SenTheme {
        // Bypass the try-catch check, which in Preview doesn't result in LocalSenAppIcon.Success
        CompositionLocalProvider(LocalSenAppIcon provides SenAppIconResult.Success(drawable)) {
            SenAboutContent(
                easterEggEnabled = preferences.easterEggEnabled,
            ) { easterEggEnabled ->
                preferences = preferences.copy(
                    easterEggEnabled = easterEggEnabled,
                )
            }
        }
    }
}
