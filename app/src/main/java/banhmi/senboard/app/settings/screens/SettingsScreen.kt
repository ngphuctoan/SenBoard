package banhmi.senboard.app.settings.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import banhmi.senboard.app.settings.routes.SenSettingsRoutes
import banhmi.senboard.app.settings.ui.SenSettingsDivider
import banhmi.senboard.app.settings.ui.SenSettingsItem
import banhmi.senboard.app.settings.ui.SenSettingsList
import banhmi.senboard.app.settings.ui.SenSettingsListContent
import banhmi.senboard.app.settings.ui.SenSettingsListGroup
import banhmi.senboard.app.settings.ui.SenScreenScaffold
import banhmi.senboard.app.settings.ui.SenScreenTopAppBar
import banhmi.senboard.ui.theme.LightRainbow

@Composable
fun SettingsScreen(onRouteNavigate: (SenSettingsRoutes) -> Unit) {
    SenScreenScaffold(topBar = { SenScreenTopAppBar("Cài đặt SenBoard") }) { innerPadding ->
        SenSettingsList(modifier = Modifier.padding(innerPadding)) {
            SenSettingsListGroup {
                SenSettingsListContent {
                    SenSettingsItem(
                        label = "Hướng dẫn",
                        supportingLabels = listOf("Cài đặt bàn phím", "Kiểm tra"),
                        icon = Icons.Rounded.QuestionMark,
                        color = LightRainbow.teal.color,
                        contentColor = LightRainbow.teal.onColor,
                        onClick = { onRouteNavigate(SenSettingsRoutes.InstructionsRoute) },
                    )
                }
            }

            SenSettingsListGroup {
                SenSettingsListContent {
                    SenSettingsItem(
                        label = "Phương thức nhập",
                        supportingLabels = listOf("Tự động viết hoa", "Phím tắt", "Gợi ý"),
                        icon = Icons.Rounded.Edit,
                        color = LightRainbow.green.color,
                        contentColor = LightRainbow.green.onColor,
                        onClick = { onRouteNavigate(SenSettingsRoutes.InputMethodSettingsRoute) },
                    )
                    SenSettingsDivider()
                    SenSettingsItem(
                        label = "Giao diện",
                        supportingLabels = listOf("Viền bàn phím", "Phông chữ"),
                        icon = Icons.Rounded.Palette,
                        color = LightRainbow.blue.color,
                        contentColor = LightRainbow.blue.onColor,
                        onClick = { onRouteNavigate(SenSettingsRoutes.AppearanceSettingsRoute) },
                    )
                    SenSettingsDivider()
                    SenSettingsItem(
                        label = "Âm thanh & Haptic",
                        supportingLabels = listOf("Haptic", "Cường độ haptic", "Âm lượng"),
                        icon = Icons.AutoMirrored.Rounded.VolumeUp,
                        color = LightRainbow.orange.color,
                        contentColor = LightRainbow.orange.onColor,
                        onClick = { onRouteNavigate(SenSettingsRoutes.SoundsAndHapticsSettingsRoute) },
                    )
                }
            }

            SenSettingsListGroup {
                SenSettingsListContent {
                    SenSettingsItem(
                        label = "Giới thiệu",
                        supportingLabels = listOf("Phiên bản", "Giấy phép", "Mã nguồn"),
                        icon = Icons.Rounded.Info,
                        color = LightRainbow.indigo.color,
                        contentColor = LightRainbow.indigo.onColor,
                        onClick = { onRouteNavigate(SenSettingsRoutes.AboutRoute) },
                    )
                }
            }
        }
    }
}
