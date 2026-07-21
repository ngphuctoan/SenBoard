package banhmi.senboard.app.settings.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Redeem
import androidx.compose.material.icons.rounded.SpaceBar
import androidx.compose.material.icons.rounded.Upload
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import banhmi.senboard.shared.settings.SenSettingsViewModel
import banhmi.senboard.app.settings.models.SenSettingsItemAction
import banhmi.senboard.app.settings.ui.SenSettingsDivider
import banhmi.senboard.app.settings.ui.SenSettingsItem
import banhmi.senboard.app.settings.ui.SenSettingsList
import banhmi.senboard.app.settings.ui.SenSettingsListContent
import banhmi.senboard.app.settings.ui.SenSettingsListGroup
import banhmi.senboard.app.settings.ui.SenScreenScaffold
import banhmi.senboard.app.settings.ui.SenScreenTopAppBar

@Composable
fun InputMethodSettingsScreen(
    onBackClick: () -> Unit,
    viewModel: SenSettingsViewModel = viewModel(factory = SenSettingsViewModel.Factory),
) {
    val state by viewModel.inputMethodState.collectAsStateWithLifecycle()

    var showEasterEggToggle by remember { mutableStateOf(false) }

    LaunchedEffect(showEasterEggToggle, state.easterEggEnabled) {
        if (!showEasterEggToggle && state.easterEggEnabled) showEasterEggToggle = true
    }

    SenScreenScaffold(topBar = { SenScreenTopAppBar("Phương thức nhập", onBackClick) }) { innerPadding ->
        SenSettingsList(modifier = Modifier.padding(innerPadding)) {
            SenSettingsListGroup(isSubMenu = true) {
                SenSettingsListContent {
                    SenSettingsItem(
                        label = "Dấu hoa đầu câu",
                        supportingLabels = listOf("Bật phím Shift khi bắt đầu một câu mới"),
                        icon = Icons.Rounded.Upload,
                        action = SenSettingsItemAction.Trailing({
                            Switch(
                                checked = state.autoCapitalizationEnabled,
                                onCheckedChange = { viewModel.updateAutoCapitalizationEnabled(!state.autoCapitalizationEnabled) },
                            )
                        }),
                        onClick = { viewModel.updateAutoCapitalizationEnabled(!state.autoCapitalizationEnabled) },
                    )
                    SenSettingsDivider()
                    SenSettingsItem(
                        label = "Phím tắt dấu cách",
                        supportingLabels = listOf("Tự động thêm dấu chấm khi nhấn phím cách hai lần"),
                        icon = Icons.Rounded.SpaceBar,
                        action = SenSettingsItemAction.Trailing({
                            Switch(
                                checked = state.spaceBarShortcutEnabled,
                                onCheckedChange = { viewModel.updateSpaceBarShortcutEnabled(!state.spaceBarShortcutEnabled) },
                            )
                        }),
                        onClick = { viewModel.updateSpaceBarShortcutEnabled(!state.spaceBarShortcutEnabled) },
                    )
                }
            }

            if (showEasterEggToggle) {
                SenSettingsListGroup(isSubMenu = true) {
                    SenSettingsListContent {
                        SenSettingsItem(
                            label = "Easter egg",
                            supportingLabels = listOf("Có thể bật lại tại mục Giới thiệu"),
                            icon = Icons.Rounded.Redeem,
                            action = SenSettingsItemAction.Trailing({
                                Switch(
                                    checked = state.easterEggEnabled,
                                    onCheckedChange = { viewModel.updateEasterEggEnabled(!state.easterEggEnabled) },
                                )
                            }),
                            onClick = { viewModel.updateEasterEggEnabled(!state.easterEggEnabled) },
                        )
                    }
                }
            }
        }
    }
}
