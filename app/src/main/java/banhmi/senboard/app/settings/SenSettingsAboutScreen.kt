package banhmi.senboard.app.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import banhmi.senboard.BuildConfig
import banhmi.senboard.app.settings.ui.SenSettingsIcon
import banhmi.senboard.app.settings.ui.SenSettingsMenu
import banhmi.senboard.app.settings.ui.SenSettingsScaffold
import banhmi.senboard.app.settings.ui.SenSettingsSupportingLabels
import banhmi.senboard.ui.theme.SenBoardTheme

@Composable
fun SenSettingsAboutScreen(onNavigateBack: () -> Unit) {
    val context = LocalContext.current

    SenSettingsScaffold { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = innerPadding,
            verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap),
        ) {
            item {
                SenSettingsMenu(
                    shapes = ListItemDefaults.segmentedShapes(index = 0, count = 3),
                    headlineContent = { Text("Phiên bản ứng dụng") },
                    supportingContent = {
                        SenSettingsSupportingLabels(BuildConfig.VERSION_NAME)
                    },
                    onClick = {},
                )
            }
            item {
                SenSettingsMenu(
                    shapes = ListItemDefaults.segmentedShapes(index = 1, count = 3),
                    headlineContent = { Text("Giấy phép") },
                    supportingContent = {
                        SenSettingsSupportingLabels("Apache-2.0")
                    },
                    trailingContent = {
                        SenSettingsIcon(
                            icon = Icons.AutoMirrored.Filled.OpenInNew,
                        )
                    },
                    onClick = {},
                )
            }
            item {
                SenSettingsMenu(
                    shapes = ListItemDefaults.segmentedShapes(index = 2, count = 3),
                    headlineContent = { Text("Mã nguồn") },
                    supportingContent = {
                        SenSettingsSupportingLabels("Yêu cầu tài khoản trường")
                    },
                    trailingContent = {
                        SenSettingsIcon(
                            icon = Icons.AutoMirrored.Filled.OpenInNew,
                        )
                    },
                    onClick = {},
                )
            }
        }
    }
}

@Composable
@Preview(device = "id:pixel_10_pro_xl", showSystemUi = true)
fun SenSettingsAboutScreenPreview() {
    SenBoardTheme(dynamicColor = false) {
        SenSettingsAboutScreen(onNavigateBack = {})
    }
}
