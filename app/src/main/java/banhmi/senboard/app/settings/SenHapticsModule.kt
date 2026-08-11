package banhmi.senboard.app.settings

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSliderState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import banhmi.senboard.app.annotations.SenPreviewCommon
import banhmi.senboard.app.navigation.SenEntryProviderInstaller
import banhmi.senboard.app.ui.SenColumn
import banhmi.senboard.app.ui.SenColumnSpacer
import banhmi.senboard.app.ui.SenMenu
import banhmi.senboard.app.ui.SenMenuDefaults
import banhmi.senboard.app.ui.SenScaffold
import banhmi.senboard.app.ui.outOf
import banhmi.senboard.ui.theme.SenTheme
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

object SenHaptics

@Module
@InstallIn(ActivityRetainedComponent::class)
object SenHapticsModule {
    @Provides
    @IntoSet
    fun provideEntryProviderInstaller(): SenEntryProviderInstaller = {
        entry<SenHaptics> {
            SenHapticsScreen()
        }
    }
}

@Composable
fun SenHapticsScreen() {
    SenScaffold(
        topBar = {},
    ) { innerPadding ->
        SenColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
        ) {
            item {
                SenMenu(
                    checked = true,
                    onCheckedChange = {},
                    shapes = SenMenuDefaults.circleShapes(),
                    trailingContent = {
                        Switch(
                            checked = true,
                            onCheckedChange = null,
                        )
                    },
                    contentPadding = SenMenuDefaults.CircleContentPadding,
                    colors = SenMenuDefaults.primaryColors(),
                    modifier = Modifier.padding(vertical = 8.dp),
                ) {
                    Text("Sử dụng haptic cho các phím")
                }
            }

            item { SenColumnSpacer() }

            item {
                SenMenu(
                    shapes = SenMenuDefaults.segmentedShapes(0 outOf 1),
                    supportingContent = {
                        Slider(
                            state = rememberSliderState(
                                value = 66f,
                                steps = 2,
                                valueRange = 0f..100f,
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp),
                        )
                    },
                ) {
                    Text("Độ mạnh của haptic")
                }
            }
        }
    }
}

@Composable
@SenPreviewCommon
fun SenHapticsScreenPreview() {
    SenTheme(dynamicColor = false) {
        SenHapticsScreen()
    }
}
