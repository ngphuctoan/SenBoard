package banhmi.senboard.app.settings.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BorderStyle
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.OpenInFull
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
fun AppearanceSettingsScreen(
    onBackClick: () -> Unit,
    viewModel: SenSettingsViewModel = viewModel(factory = SenSettingsViewModel.Factory),
) {
    val state by viewModel.appearanceState.collectAsStateWithLifecycle()

    SenScreenScaffold(topBar = { SenScreenTopAppBar("Giao diện", onBackClick) }) { innerPadding ->
        SenSettingsList(modifier = Modifier.padding(innerPadding)) {
            SenSettingsListGroup(isSubMenu = true) {
                SenSettingsListContent {
                    SenSettingsItem(
                        label = "Chủ đề OLED",
                        supportingLabels = listOf("Nhìn khá là ngầu đấy chứ!"),
                        icon = Icons.Rounded.DarkMode,
                        action = SenSettingsItemAction.Trailing({
                            Switch(
                                checked = state.oledThemeEnabled,
                                onCheckedChange = { viewModel.updateOledThemeEnabled(!state.oledThemeEnabled) },
                            )
                        }),
                        onClick = { viewModel.updateOledThemeEnabled(!state.oledThemeEnabled) },
                    )
                }
            }
            SenSettingsListGroup(isSubMenu = true) {
                SenSettingsListContent {
                    SenSettingsItem(
                        label = "Mở rộng bàn phím",
                        supportingLabels = listOf("Hiển thị bàn phím trên toàn bộ chiều rộng màn hình"),
                        icon = Icons.Rounded.OpenInFull,
                        action = SenSettingsItemAction.Trailing({
                            Switch(
                                checked = state.fullWidthKeyboard,
                                onCheckedChange = { viewModel.updateFullWidthKeyboard(!state.fullWidthKeyboard) },
                            )
                        }),
                        onClick = { viewModel.updateFullWidthKeyboard(!state.fullWidthKeyboard) },
                    )
                    SenSettingsDivider()
                    SenSettingsItem(
                        label = "Nền phím",
                        supportingLabels = listOf("Tăng độ tương phản cho các phím"),
                        icon = Icons.Rounded.BorderStyle,
                        action = SenSettingsItemAction.Trailing({
                            Switch(
                                checked = state.showKeyBackground,
                                onCheckedChange = { viewModel.updateShowKeyBackground(!state.showKeyBackground) },
                            )
                        }),
                        onClick = { viewModel.updateShowKeyBackground(!state.showKeyBackground) },
                    )
                }
            }
        }
    }
}
