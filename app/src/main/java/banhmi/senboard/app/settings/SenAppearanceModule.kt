package banhmi.senboard.app.settings

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Numbers
import androidx.compose.material.icons.outlined.Tonality
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
import banhmi.senboard.app.ui.SenIcon
import banhmi.senboard.app.ui.SenMenu
import banhmi.senboard.app.ui.SenMenuDefaults
import banhmi.senboard.app.ui.SenScaffold
import banhmi.senboard.app.ui.SenSwitch
import banhmi.senboard.app.ui.SenTopBar
import banhmi.senboard.app.ui.SenTopBarBackButton
import banhmi.senboard.app.ui.rememberSenTopBarState
import banhmi.senboard.app.ui.segmentedPadding
import banhmi.senboard.data.preferences.SenPreferences
import banhmi.senboard.data.preferences.SenPreferencesViewModel
import banhmi.senboard.ui.theme.SenTheme
import banhmi.senboard.utils.outOf
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

object SenAppearance

@Module
@InstallIn(ActivityRetainedComponent::class)
object SenAppearanceModule {
    @Provides
    @IntoSet
    fun provideEntryProviderInstaller(
        navigator: SenNavigator,
    ): SenEntryProviderInstaller = {
        entry<SenAppearance> {
            SenAppearanceScreen(navigator)
        }
    }
}

@Composable
fun SenAppearanceScreen(
    navigator: SenNavigator,
    preferencesViewModel: SenPreferencesViewModel = hiltViewModel(),
) {
    val preferences by preferencesViewModel.preferences.collectAsStateWithLifecycle()

    SenAppearanceContent(
        onNavigateBack = navigator::goBack,
        keyBackgroundEnabled = preferences.keyBackgroundEnabled,
        keyBackgroundShadowEnabled = preferences.keyBackgroundShadowEnabled,
        numberRowEnabled = preferences.numberRowEnabled,
        onKeyBackgroundEnabledUpdate = preferencesViewModel::updateKeyBackgroundEnabled,
        onKeyBackgroundShadowEnabledUpdate = preferencesViewModel::updateKeyBackgroundShadowEnabled,
        onNumberRowEnabledUpdate = preferencesViewModel::updateNumberRowEnabled,
    )
}

@Composable
fun SenAppearanceContent(
    onNavigateBack: () -> Unit = {},
    keyBackgroundEnabled: Boolean,
    keyBackgroundShadowEnabled: Boolean,
    numberRowEnabled: Boolean,
    onKeyBackgroundEnabledUpdate: (Boolean) -> Unit,
    onKeyBackgroundShadowEnabledUpdate: (Boolean) -> Unit,
    onNumberRowEnabledUpdate: (Boolean) -> Unit,
) {
    val topAppBarState = rememberSenTopBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(topAppBarState)

    SenScaffold(
        topBar = {
            SenTopBar(
                title = { Text("Giao diện") },
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
                    shapes = SenMenuDefaults.segmentedShapes(0 outOf 3),
                    onClick = { onNumberRowEnabledUpdate(!numberRowEnabled) },
                    supportingContent = { Text("Thêm hàng phím số 1-0 ở đầu bàn phím (Tiện lợi cho VNI)") },
                    leadingContent = {
                        SenIcon(
                            icon = Icons.Outlined.Numbers,
                            description = "Hàng phím số (Number Row)",
                        )
                    },
                    trailingContent = {
                        SenSwitch(
                            checked = numberRowEnabled,
                            onCheckedChange = null,
                        )
                    },
                    modifier = Modifier.segmentedPadding(),
                ) {
                    Text("Hàng phím số (Number Row)")
                }
            }

            item {
                SenMenu(
                    shapes = SenMenuDefaults.segmentedShapes(1 outOf 3),
                    onClick = { onKeyBackgroundEnabledUpdate(!keyBackgroundEnabled) },
                    leadingContent = {
                        SenIcon(
                            icon = Icons.Outlined.GridView,
                            description = "Hiển thị nền phím",
                        )
                    },
                    trailingContent = {
                        SenSwitch(
                            checked = keyBackgroundEnabled,
                            onCheckedChange = null,
                        )
                    },
                    modifier = Modifier.segmentedPadding(),
                ) {
                    Text("Hiển thị nền phím")
                }
            }

            item {
                SenMenu(
                    shapes = SenMenuDefaults.segmentedShapes(2 outOf 3),
                    onClick = { onKeyBackgroundShadowEnabledUpdate(!keyBackgroundShadowEnabled) },
                    supportingContent = { Text("Yêu cầu hiển thị nền phím") },
                    leadingContent = {
                        SenIcon(
                            icon = Icons.Outlined.Tonality,
                            description = "Đổ bóng cho nền phím",
                        )
                    },
                    trailingContent = {
                        SenSwitch(
                            checked = keyBackgroundShadowEnabled,
                            onCheckedChange = null,
                        )
                    },
                ) {
                    Text("Đổ bóng cho nền phím")
                }
            }
        }
    }
}

@Composable
@SenPreviewCommon
fun SenAppearanceScreenPreview() {
    var preferences by remember { mutableStateOf(SenPreferences()) }

    SenTheme {
        SenAppearanceContent(
            keyBackgroundEnabled = preferences.keyBackgroundEnabled,
            keyBackgroundShadowEnabled = preferences.keyBackgroundShadowEnabled,
            numberRowEnabled = preferences.numberRowEnabled,
            onKeyBackgroundEnabledUpdate = { keyBackgroundEnabled ->
                preferences = preferences.copy(keyBackgroundEnabled = keyBackgroundEnabled)
            },
            onKeyBackgroundShadowEnabledUpdate = { keyBackgroundShadowEnabled ->
                preferences =
                    preferences.copy(keyBackgroundShadowEnabled = keyBackgroundShadowEnabled)
            },
            onNumberRowEnabledUpdate = { numberRowEnabled ->
                preferences = preferences.copy(numberRowEnabled = numberRowEnabled)
            },
        )
    }
}
