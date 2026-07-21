package banhmi.senboard.app.settings.screens

import androidx.compose.runtime.Composable
import banhmi.senboard.app.settings.ui.SenSettingsItem
import banhmi.senboard.app.settings.ui.SenSettingsList
import banhmi.senboard.app.settings.ui.SenSettingsListContent
import banhmi.senboard.app.settings.ui.SenSettingsListGroup

@Composable
fun AppearanceSettingsScreen() {
    SenSettingsList {
        SenSettingsListGroup(isSubMenu = true) {
            SenSettingsListContent {
                SenSettingsItem(label = "Ở đây không có gì cả :b")
            }
        }
    }
}
