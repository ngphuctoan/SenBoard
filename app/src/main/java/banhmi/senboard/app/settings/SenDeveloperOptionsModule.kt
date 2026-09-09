package banhmi.senboard.app.settings

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import banhmi.senboard.annotations.SenPreviewCommon
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

object SenDeveloperOptions

@Module
@InstallIn(ActivityRetainedComponent::class)
object SenDeveloperOptionsModule {
    @Provides
    @IntoSet
    fun provideEntryProviderInstaller(
        navigator: SenNavigator,
    ): SenEntryProviderInstaller = {
        entry<SenDeveloperOptions> {
            SenDeveloperOptionsScreen(navigator)
        }
    }
}

@Composable
fun SenDeveloperOptionsScreen(
    navigator: SenNavigator,
    preferencesViewModel: SenPreferencesViewModel = hiltViewModel(),
) {
    val preferences by preferencesViewModel.preferencesState.collectAsStateWithLifecycle()

    SenDeveloperOptionsContent(
        onNavigateBack = navigator::goBack,
        developerOptionsEnabled = preferences.developerOptionsEnabled,
        statisticsEnabled = preferences.statisticsEnabled,
        onDeveloperOptionsEnabledUpdate = preferencesViewModel::updateDeveloperOptionsEnabled,
        onStatisticsEnabledUpdate = preferencesViewModel::updateStatisticsEnabled,
    )
}

@Composable
fun SenDeveloperOptionsContent(
    onNavigateBack: () -> Unit = {},
    developerOptionsEnabled: Boolean,
    statisticsEnabled: Boolean,
    onDeveloperOptionsEnabledUpdate: (Boolean) -> Unit,
    onStatisticsEnabledUpdate: (Boolean) -> Unit,
) {
    val topAppBarState = rememberSenTopBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(topAppBarState)

    SenScaffold(
        topBar = {
            SenTopBar(
                title = { Text("Chế độ nhà phát triển") },
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
                    checked = developerOptionsEnabled,
                    onCheckedChange = { onDeveloperOptionsEnabledUpdate(!developerOptionsEnabled) },
                    shapes = SenMenuDefaults.circleShapes(),
                    trailingContent = {
                        SenSwitch(
                            checked = developerOptionsEnabled,
                            onCheckedChange = null,
                        )
                    },
                    contentPadding = SenMenuDefaults.CircleContentPadding,
                    colors = SenMenuDefaults.primaryColors(),
                    modifier = Modifier
                        .padding(SenMenuDefaults.CirclePadding)
                        .lastSegmentedPadding(),
                ) {
                    Text("Bật chế độ nhà phát triển")
                }
            }

            item {
                SenMenu(
                    shapes = SenMenuDefaults.segmentedShapes(0 outOf 1),
                    supportingContent = { Text("Thời gian, quãng đường gõ phím") },
                    trailingContent = {
                        SenSwitch(
                            checked = statisticsEnabled,
                            onCheckedChange = null,
                        )
                    },
                    onClick = { onStatisticsEnabledUpdate(!statisticsEnabled) },
                    modifier = Modifier.lastSegmentedPadding(),
                ) {
                    Text("Hiển thị thống kê")
                }
            }
        }
    }
}

@Composable
@SenPreviewCommon
fun SenDeveloperOptionsScreenPreview() {
    var preferences by remember { mutableStateOf(SenPreferences()) }

    SenTheme {
        SenDeveloperOptionsContent(
            developerOptionsEnabled = preferences.developerOptionsEnabled,
            statisticsEnabled = preferences.statisticsEnabled,
            onDeveloperOptionsEnabledUpdate = { developerOptionsEnabled ->
                preferences = preferences.copy(developerOptionsEnabled = developerOptionsEnabled)
            },
            onStatisticsEnabledUpdate = { statisticsEnabled ->
                preferences = preferences.copy(statisticsEnabled = statisticsEnabled)
            },
        )
    }
}
