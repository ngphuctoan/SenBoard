package banhmi.senboard.app.settings.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.Vibration
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import banhmi.senboard.app.settings.data.SenSettingsViewModel
import banhmi.senboard.app.settings.models.SenSettingsItemAction
import banhmi.senboard.app.settings.ui.SenSettingsDivider
import banhmi.senboard.app.settings.ui.SenSettingsItem
import banhmi.senboard.app.settings.ui.SenSettingsList
import banhmi.senboard.app.settings.ui.SenSettingsListContent
import banhmi.senboard.app.settings.ui.SenSettingsListGroup
import banhmi.senboard.app.settings.ui.SenSettingsListHeader

@Composable
fun SoundsAndHapticsSettingsScreen(viewModel: SenSettingsViewModel = viewModel(factory = SenSettingsViewModel.Factory)) {
    val state by viewModel.soundsAndHapticsState.collectAsStateWithLifecycle()

    SenSettingsList {
        SenSettingsListGroup(isSubMenu = true) {
            SenSettingsListHeader("Haptic")
            SenSettingsListContent {
                SenSettingsItem(
                    label = "Cường độ haptic",
                    icon = Icons.Rounded.Vibration,
                    action = SenSettingsItemAction.Bottom({
                        Slider(
                            steps = 2,
                            valueRange = 0f..100f,
                            value = state.hapticIntensity.toFloat(),
                            onValueChange = { viewModel.updateHapticIntensity(it.toInt()) },
                        )
                    }),
                )
                SenSettingsDivider()
                SenSettingsItem(
                    label = "Âm lượng phím",
                    icon = Icons.AutoMirrored.Rounded.VolumeUp,
                    action = SenSettingsItemAction.Bottom({
                        Slider(
                            steps = 9,
                            valueRange = 0f..100f,
                            value = state.soundVolume.toFloat(),
                            onValueChange = { viewModel.updateSoundVolume(it.toInt()) },
                        )
                    }),
                )
            }
        }
    }
}
