package banhmi.senboard.app.settings.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Keyboard
import androidx.compose.material.icons.rounded.Redeem
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import banhmi.senboard.app.settings.SenBoardPreferences
import banhmi.senboard.app.settings.data.SenSettingsViewModel
import banhmi.senboard.app.settings.models.SenSettingsItemAction
import banhmi.senboard.app.settings.ui.SenSettingsItem
import banhmi.senboard.app.settings.ui.SenSettingsList
import banhmi.senboard.app.settings.ui.SenSettingsListContent
import banhmi.senboard.app.settings.ui.SenSettingsListGroup

@Composable
fun InputMethodSettingsScreen(viewModel: SenSettingsViewModel = viewModel(factory = SenSettingsViewModel.Factory)) {
    val context = LocalContext.current
    val prefs = remember { SenBoardPreferences(context) }
    var currentMode by remember { mutableStateOf(prefs.typingMode) }
    val state by viewModel.inputMethodState.collectAsStateWithLifecycle()

    var showEasterEggToggle by remember { mutableStateOf(false) }

    LaunchedEffect(showEasterEggToggle, state.easterEggEnabled) {
        if (!showEasterEggToggle && state.easterEggEnabled) showEasterEggToggle = true
    }

    SenSettingsList {
        SenSettingsListGroup(isSubMenu = true) {
            SenSettingsListContent {
                SenSettingsItem(
                    label = "Telex",
                    supportingLabels = listOf("Phương thức gõ tiếng Việt tiêu chuẩn"),
                    icon = Icons.Outlined.Keyboard,
                    action = if (currentMode == "telex") {
                        SenSettingsItemAction.Trailing({
                            Icon(Icons.Outlined.Check, contentDescription = "Đã chọn")
                        })
                    } else SenSettingsItemAction.None,
                    onClick = {
                        prefs.typingMode = "telex"
                        currentMode = "telex"
                    }
                )

                SenSettingsItem(
                    label = "Chữ Việt Song Song (CVNSS 4.0)",
                    supportingLabels = listOf("Bộ gõ tốc ký cực ngắn không dùng phím dấu thanh"),
                    icon = Icons.Outlined.Keyboard,
                    action = if (currentMode == "cvnss") {
                        SenSettingsItemAction.Trailing({
                            Icon(Icons.Outlined.Check, contentDescription = "Đã chọn")
                        })
                    } else SenSettingsItemAction.None,
                    onClick = {
                        prefs.typingMode = "cvnss"
                        currentMode = "cvnss"
                    }
                )

                SenSettingsItem(
                    label = "VNI",
                    supportingLabels = listOf("Phương thức gõ tiếng Việt bằng phím số"),
                    icon = Icons.Outlined.Keyboard,
                    action = if (currentMode == "vni") {
                        SenSettingsItemAction.Trailing({
                            Icon(Icons.Outlined.Check, contentDescription = "Đã chọn")
                        })
                    } else SenSettingsItemAction.None,
                    onClick = {
                        prefs.typingMode = "vni"
                        currentMode = "vni"
                    }
                )

                if (showEasterEggToggle) {
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
