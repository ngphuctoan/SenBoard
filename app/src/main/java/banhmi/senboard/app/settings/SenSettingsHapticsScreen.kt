package banhmi.senboard.app.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.rememberSliderState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import banhmi.senboard.app.settings.ui.SenSettingsLabel
import banhmi.senboard.app.settings.ui.SenSettingsMenu
import banhmi.senboard.app.settings.ui.SenSettingsMenuDefaults
import banhmi.senboard.app.settings.ui.SenSettingsMenuExtra
import banhmi.senboard.app.settings.ui.SenSettingsScaffold
import banhmi.senboard.app.settings.ui.SenSettingsTitle
import banhmi.senboard.app.settings.ui.SenSettingsTopBar
import banhmi.senboard.app.settings.ui.senSettingsVerticalSpacer
import banhmi.senboard.ui.theme.SenBoardTheme

@Composable
fun SenSettingsHapticsScreen(onNavigateBack: () -> Unit) {
    SenSettingsScaffold(
        topBar = {
            SenSettingsTopBar(
                title = { SenSettingsTitle("Haptic") },
                onNavigateBack = onNavigateBack,
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = innerPadding,
            verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap),
        ) {
            item {
                SenSettingsMenu(
                    shapes = SenSettingsMenuDefaults.pillShapes(),
                    headlineContent = { SenSettingsLabel("Sử dụng haptic") },
                    trailingContent = {
                        Switch(checked = true, onCheckedChange = {})
                    },
                    outerPadding = SenSettingsMenuDefaults.PillOuterPadding,
                    contentPadding = SenSettingsMenuDefaults.PillContentPadding,
                    colors = SenSettingsMenuDefaults.primaryContainerColors(),
                )
            }

            item { Spacer(modifier = Modifier.senSettingsVerticalSpacer()) }

            item {
                SenSettingsMenu(
                    shapes = ListItemDefaults.segmentedShapes(index = 0, count = 1),
                    headlineContent = { SenSettingsLabel("Độ mạnh haptic") },
                    supportingContent = {
                        SenSettingsMenuExtra {
                            Slider(state = rememberSliderState(0.25f))
                        }
                    },
                )
            }

            item { Spacer(modifier = Modifier.senSettingsVerticalSpacer()) }
        }
    }
}

@Composable
@Preview(
    device = "id:pixel_10_pro_xl",
    showSystemUi = true,
)
fun SenSettingsHapticsScreenPreview() {
    SenBoardTheme(dynamicColor = false) {
        SenSettingsHapticsScreen(onNavigateBack = {})
    }
}
