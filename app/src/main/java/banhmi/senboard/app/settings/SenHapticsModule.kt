package banhmi.senboard.app.settings

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import banhmi.senboard.app.annotations.SenPreviewCommon
import banhmi.senboard.app.navigation.SenEntryProviderInstaller
import banhmi.senboard.app.navigation.SenNavigator
import banhmi.senboard.app.ui.SenColumn
import banhmi.senboard.app.ui.SenMenu
import banhmi.senboard.app.ui.SenMenuDefaults
import banhmi.senboard.app.ui.SenScaffold
import banhmi.senboard.app.ui.SenSwitch
import banhmi.senboard.app.ui.SenTopBar
import banhmi.senboard.app.ui.SenTopBarBackButton
import banhmi.senboard.app.ui.lastSegmentedPadding
import banhmi.senboard.app.ui.rememberSenTopBarState
import banhmi.senboard.data.preferences.SenPreferences
import banhmi.senboard.data.preferences.SenPreferencesViewModel
import banhmi.senboard.ui.theme.SenTheme
import banhmi.senboard.utils.outOf
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

object SenHaptics

@Module
@InstallIn(ActivityRetainedComponent::class)
object SenHapticsModule {
    @Provides
    @IntoSet
    fun provideEntryProviderInstaller(
        navigator: SenNavigator,
    ): SenEntryProviderInstaller = {
        entry<SenHaptics> {
            SenHapticsScreen(navigator)
        }
    }
}

@Composable
fun SenHapticsScreen(
    navigator: SenNavigator,
    preferencesViewModel: SenPreferencesViewModel = hiltViewModel(),
) {
    val preferences by preferencesViewModel.preferencesState.collectAsStateWithLifecycle()

    SenHapticsContent(
        onNavigateBack = navigator::goBack,
        hapticsEnabled = preferences.hapticsEnabled,
        hapticsIntensity = preferences.hapticsIntensity,
        onHapticsEnabledUpdate = preferencesViewModel::updateHapticsEnabled,
        onHapticsIntensityUpdate = preferencesViewModel::updateHapticsIntensity,
    )
}

@Composable
fun SenHapticsContent(
    onNavigateBack: () -> Unit = {},
    hapticsEnabled: Boolean,
    hapticsIntensity: Int,
    onHapticsEnabledUpdate: (Boolean) -> Unit,
    onHapticsIntensityUpdate: (Int) -> Unit,
) {
    val topAppBarState = rememberSenTopBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(topAppBarState)

    SenScaffold(
        topBar = {
            SenTopBar(
                title = { Text("Haptic") },
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
                SenMenu(
                    checked = hapticsEnabled,
                    onCheckedChange = { onHapticsEnabledUpdate(!hapticsEnabled) },
                    shapes = SenMenuDefaults.circleShapes(),
                    trailingContent = {
                        SenSwitch(
                            checked = hapticsEnabled,
                            onCheckedChange = null,
                        )
                    },
                    contentPadding = SenMenuDefaults.CircleContentPadding,
                    colors = SenMenuDefaults.primaryColors(),
                    modifier = Modifier
                        .padding(SenMenuDefaults.CirclePadding)
                        .lastSegmentedPadding(),
                ) {
                    Text("Sử dụng haptic")
                }
            }

            item {
                SenMenu(
                    enabled = hapticsEnabled,
                    shapes = SenMenuDefaults.segmentedShapes(0 outOf 1),
                    supportingContent = {
                        Slider(
                            enabled = hapticsEnabled,
                            steps = 2,
                            valueRange = 0f..100f,
                            value = hapticsIntensity.toFloat(),
                            onValueChange = { value -> onHapticsIntensityUpdate(value.toInt()) },
                            modifier = Modifier.padding(horizontal = 8.dp),
                        )
                    },
                ) {
                    Text("Độ mạnh của haptic")
                }
            }
        }
    }
}

@Composable
@SenPreviewCommon
fun SenHapticsScreenPreview() {
    var preferences by remember { mutableStateOf(SenPreferences()) }

    SenTheme {
        SenHapticsContent(
            hapticsEnabled = preferences.hapticsEnabled,
            hapticsIntensity = preferences.hapticsIntensity,
            onHapticsEnabledUpdate = { hapticsEnabled ->
                preferences = preferences.copy(hapticsEnabled = hapticsEnabled)
            },
            onHapticsIntensityUpdate = { hapticsIntensity ->
                preferences = preferences.copy(hapticsIntensity = hapticsIntensity)
            },
        )
    }
}
