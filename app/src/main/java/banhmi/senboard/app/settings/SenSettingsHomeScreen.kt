package banhmi.senboard.app.settings

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PermDeviceInformation
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ListItemShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import banhmi.senboard.app.navigation.SenSettingsAbout
import banhmi.senboard.app.navigation.SenSettingsAppearance
import banhmi.senboard.app.navigation.SenSettingsHaptics
import banhmi.senboard.app.navigation.SenSettingsHelp
import banhmi.senboard.app.navigation.SenSettingsInputMethod
import banhmi.senboard.app.settings.ui.SenSettingsIcon
import banhmi.senboard.app.settings.ui.SenSettingsIconDefaults
import banhmi.senboard.app.settings.ui.SenSettingsInputTester
import banhmi.senboard.app.settings.ui.SenSettingsLabel
import banhmi.senboard.app.settings.ui.SenSettingsMenu
import banhmi.senboard.app.settings.ui.SenSettingsScaffold
import banhmi.senboard.app.settings.ui.SenSettingsSupportingLabels
import banhmi.senboard.app.settings.ui.senSettingsVerticalSpacer
import banhmi.senboard.ui.theme.M3RefPalette
import banhmi.senboard.ui.theme.SenBoardTheme
import banhmi.senboard.ui.theme.m3RefPaletteCyan
import banhmi.senboard.ui.theme.m3RefPalettePink
import banhmi.senboard.ui.theme.m3RefPaletteYellow

@Composable
fun SenSettingsHomeTopBar(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = content,
        contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp, start = 4.dp, end = 4.dp),
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        modifier = modifier,
    )
}

@Composable
fun SenSettingsHomeKeyboardDisabledWarning(
    shapes: ListItemShapes,
    m3RefPalette: M3RefPalette = m3RefPaletteYellow,
    darkTheme: Boolean = isSystemInDarkTheme(),
    onClick: (() -> Unit)? = null,
) {
    val containerColor = if (darkTheme) m3RefPalette.color30 else m3RefPalette.color90
    val contentColor = if (darkTheme) m3RefPalette.color90 else m3RefPalette.color30
    val supportingContentColor = if (darkTheme) m3RefPalette.color80 else m3RefPalette.color40
    val buttonColor = MaterialTheme.colorScheme.surface

    SenSettingsMenu(
        shapes = shapes,
        headlineContent = { SenSettingsLabel("Bàn phím chưa được kích hoạt") },
        supportingContent = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Kiểm tra trong Cài đặt - Trợ năng - Bàn phím")
                Button(
                    onClick = {}, // Already handled by the list item
                    colors = ButtonColors(
                        containerColor = buttonColor,
                        disabledContainerColor = buttonColor,
                        contentColor = supportingContentColor,
                        disabledContentColor = supportingContentColor,
                    ),
                ) {
                    Text("Đi đến")
                }
            }
        },
        leadingContent = {
            SenSettingsIcon(
                icon = Icons.Filled.Warning,
                sizes = SenSettingsIconDefaults.IconWithShapeSizes,
                colors = SenSettingsIconDefaults.neutralColors()
                    .copy(iconColor = supportingContentColor),
            )
        },
        colors = ListItemDefaults.segmentedColors(
            containerColor = containerColor,
            contentColor = contentColor,
            supportingContentColor = supportingContentColor,
        ),
        onClick = onClick,
    )
}

@Composable
fun SenSettingsHomeScreen(onNavigate: (Any) -> Unit) {
    SenSettingsScaffold(
        topBar = {
            SenSettingsHomeTopBar {
                SenSettingsInputTester(modifier = Modifier.fillMaxWidth())
            }
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = innerPadding,
            verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap),
        ) {
            /* item {
                SenSettingsHomeKeyboardDisabledWarning(
                    shapes = ListItemDefaults.segmentedShapes(index = 0, count = 1),
                    onClick = {},
                )
            }

            item { Spacer(modifier = Modifier.senSettingsVerticalSpacer()) } */

            // First section for general stuff like Help and About
            item {
                SenSettingsMenu(
                    shapes = ListItemDefaults.segmentedShapes(index = 0, count = 2),
                    headlineContent = { SenSettingsLabel("Hướng dẫn sử dụng") },
                    supportingContent = { SenSettingsSupportingLabels("Bộ gõ Chữ Việt Song Song") },
                    leadingContent = {
                        SenSettingsIcon(
                            icon = Icons.AutoMirrored.Filled.MenuBook,
                            sizes = SenSettingsIconDefaults.IconWithShapeSizes,
                            colors = SenSettingsIconDefaults.vibrantColors(m3RefPaletteCyan),
                        )
                    },
                    onClick = { onNavigate(SenSettingsHelp) },
                )
            }
            item {
                SenSettingsMenu(
                    shapes = ListItemDefaults.segmentedShapes(index = 1, count = 2),
                    headlineContent = { SenSettingsLabel("Giới thiệu ứng dụng") },
                    supportingContent = { SenSettingsSupportingLabels("Phiên bản", "giấy phép") },
                    leadingContent = {
                        SenSettingsIcon(
                            icon = Icons.Filled.PermDeviceInformation,
                            sizes = SenSettingsIconDefaults.IconWithShapeSizes,
                            colors = SenSettingsIconDefaults.vibrantColors(m3RefPaletteCyan),
                        )
                    },
                    onClick = { onNavigate(SenSettingsAbout) },
                )
            }

            item { Spacer(modifier = Modifier.senSettingsVerticalSpacer()) }

            // Second section for actual settings
            item {
                SenSettingsMenu(
                    shapes = ListItemDefaults.segmentedShapes(index = 0, count = 3),
                    headlineContent = { SenSettingsLabel("Phương thức nhập") },
                    supportingContent = {
                        SenSettingsSupportingLabels(
                            "Tự động viết hoa",
                            "phím tắt",
                            "gợi ý"
                        )
                    },
                    leadingContent = {
                        SenSettingsIcon(
                            icon = Icons.Filled.Keyboard,
                            sizes = SenSettingsIconDefaults.IconWithShapeSizes,
                            colors = SenSettingsIconDefaults.vibrantColors(m3RefPalettePink),
                        )
                    },
                    onClick = { onNavigate(SenSettingsInputMethod) },
                )
            }
            item {
                SenSettingsMenu(
                    shapes = ListItemDefaults.segmentedShapes(index = 1, count = 3),
                    headlineContent = { SenSettingsLabel("Giao diện") },
                    supportingContent = {
                        SenSettingsSupportingLabels(
                            "Viền phím",
                            "độ rộng bàn phím"
                        )
                    },
                    leadingContent = {
                        SenSettingsIcon(
                            icon = Icons.Filled.Palette,
                            sizes = SenSettingsIconDefaults.IconWithShapeSizes,
                            colors = SenSettingsIconDefaults.vibrantColors(m3RefPalettePink),
                        )
                    },
                    onClick = { onNavigate(SenSettingsAppearance) },
                )
            }
            item {
                SenSettingsMenu(
                    shapes = ListItemDefaults.segmentedShapes(index = 2, count = 3),
                    headlineContent = { SenSettingsLabel("Haptic") },
                    supportingContent = { SenSettingsSupportingLabels("Độ mạnh của haptic") },
                    leadingContent = {
                        SenSettingsIcon(
                            icon = Icons.AutoMirrored.Filled.VolumeUp,
                            sizes = SenSettingsIconDefaults.IconWithShapeSizes,
                            colors = SenSettingsIconDefaults.vibrantColors(m3RefPaletteYellow),
                        )
                    },
                    onClick = { onNavigate(SenSettingsHaptics) },
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
fun SenSettingsHomeScreenPreview() {
    SenBoardTheme(dynamicColor = false) {
        SenSettingsHomeScreen(onNavigate = {})
    }
}
