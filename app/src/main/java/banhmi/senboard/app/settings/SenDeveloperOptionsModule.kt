package banhmi.senboard.app.settings

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
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
import banhmi.senboard.app.annotations.SenPreviewCommon
import banhmi.senboard.app.navigation.SenEntryProviderInstaller
import banhmi.senboard.app.navigation.SenNavigator
import banhmi.senboard.app.ui.SenColumn
import banhmi.senboard.app.ui.SenDescription
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
    val preferences by preferencesViewModel.preferences.collectAsStateWithLifecycle()

    SenDeveloperOptionsContent(
        onNavigateBack = navigator::goBack,
        developerOptionsEnabled = preferences.developerOptionsEnabled,
        onDeveloperOptionsEnabledUpdate = preferencesViewModel::updateDeveloperOptionsEnabled,
    )
}

@Composable
fun SenDeveloperOptionsContent(
    onNavigateBack: () -> Unit = {},
    developerOptionsEnabled: Boolean,
    onDeveloperOptionsEnabledUpdate: (Boolean) -> Unit,
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
                SenDescription {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = "Thông tin về chế độ nhà phát triển",
                    )
                    Text("Hiện tại vẫn đang phát triển, vui lòng quay lại sau!")
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
            onDeveloperOptionsEnabledUpdate = { developerOptionsEnabled ->
                preferences = preferences.copy(developerOptionsEnabled = developerOptionsEnabled)
            },
        )
    }
}
