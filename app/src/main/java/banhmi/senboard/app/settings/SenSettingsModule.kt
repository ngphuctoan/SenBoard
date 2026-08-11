package banhmi.senboard.app.settings

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PermDeviceInformation
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import banhmi.senboard.BuildConfig
import banhmi.senboard.app.annotations.SenPreviewCommon
import banhmi.senboard.app.navigation.SenEntryProviderInstaller
import banhmi.senboard.app.navigation.SenNavigator
import banhmi.senboard.app.ui.IndexCount
import banhmi.senboard.app.ui.SenColumn
import banhmi.senboard.app.ui.SenColumnSpacer
import banhmi.senboard.app.ui.SenIcon
import banhmi.senboard.app.ui.SenIconDefaults
import banhmi.senboard.app.ui.SenMenu
import banhmi.senboard.app.ui.SenMenuDefaults
import banhmi.senboard.app.ui.SenScaffold
import banhmi.senboard.app.ui.outOf
import banhmi.senboard.ui.theme.M3RefPalette
import banhmi.senboard.ui.theme.SenTheme
import banhmi.senboard.ui.theme.m3RefPaletteCyan
import banhmi.senboard.ui.theme.m3RefPalettePink
import banhmi.senboard.ui.theme.m3RefPaletteYellow
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

object SenSettings

@Module
@InstallIn(ActivityRetainedComponent::class)
object SenSettingsModule {
    @Provides
    @IntoSet
    fun provideEntryProviderInstaller(navigator: SenNavigator): SenEntryProviderInstaller = {
        entry<SenSettings> {
            SenSettingsScreen(onNavigate = { destination -> navigator.goTo(destination) })
        }
    }
}

data class SenSettingsMenu(
    val indexCount: IndexCount,
    val destination: Any,
    val label: String,
    val supportingLabel: String,
    val icon: ImageVector,
    val iconPalette: M3RefPalette,
    val iconDescription: String? = label,
)

val Menus = listOf(
    SenSettingsMenu(
        indexCount = 0 outOf 2,
        destination = SenHelp,
        label = "Hướng dẫn sử dụng",
        supportingLabel = "Bộ gõ chữ Việt song song",
        icon = Icons.AutoMirrored.Filled.MenuBook,
        iconPalette = m3RefPaletteCyan,
    ),
    SenSettingsMenu(
        indexCount = 1 outOf 2,
        destination = SenAbout,
        label = "Giới thiệu ứng dụng",
        supportingLabel = BuildConfig.VERSION_NAME,
        icon = Icons.Filled.PermDeviceInformation,
        iconPalette = m3RefPaletteCyan,
    ),
    SenSettingsMenu(
        indexCount = 0 outOf 3,
        destination = SenInputMethod,
        label = "Phương thức nhập",
        supportingLabel = "Tự động viết hoa, phím tắt, gợi ý",
        icon = Icons.Filled.Keyboard,
        iconPalette = m3RefPalettePink,
    ),
    SenSettingsMenu(
        indexCount = 1 outOf 3,
        destination = SenAppearance,
        label = "Giao diện",
        supportingLabel = "Viền phím, độ rộng bàn phím",
        icon = Icons.Filled.Palette,
        iconPalette = m3RefPalettePink,
    ),
    SenSettingsMenu(
        indexCount = 2 outOf 3,
        destination = SenHaptics,
        label = "Haptic",
        supportingLabel = "Độ mạnh của haptic",
        icon = Icons.AutoMirrored.Filled.VolumeUp,
        iconPalette = m3RefPaletteYellow,
    ),
)

@Composable
fun SenInputTester(modifier: Modifier = Modifier) {
    val state = rememberTextFieldState()

    val textStyle: TextStyle = LocalTextStyle.current.copy(fontSize = 20.sp)

    val containerColor: Color = MaterialTheme.colorScheme.surface
    val indicatorColor: Color = Color.Transparent

    TextField(
        state = state,
        placeholder = { Text(text = "Nhấp để gõ thử", style = textStyle) },
        textStyle = textStyle,
        shape = CircleShape,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = containerColor,
            unfocusedContainerColor = containerColor,
            disabledContainerColor = containerColor,
            errorContainerColor = containerColor,
            focusedIndicatorColor = indicatorColor,
            unfocusedIndicatorColor = indicatorColor,
            disabledIndicatorColor = indicatorColor,
            errorIndicatorColor = indicatorColor,
        ),
        contentPadding = PaddingValues(24.dp),
        modifier = modifier.border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant,
            shape = CircleShape,
        ),
    )
}

@Composable
fun SenSettingsScreen(onNavigate: (Any) -> Unit) {
    SenScaffold(
        topBar = {
            SenInputTester(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(
                        top = 0.dp,
                        bottom = 8.dp,
                        start = 16.dp,
                        end = 16.dp,
                    ),
            )
        },
    ) { innerPadding ->
        SenColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
        ) {
            itemsIndexed(Menus) { index, menu ->
                val isLastShape = menu.indexCount.index == menu.indexCount.count - 1
                val isLastMenu = index == Menus.lastIndex

                SenMenu(
                    shapes = SenMenuDefaults.segmentedShapes(menu.indexCount),
                    supportingContent = { Text(menu.supportingLabel) },
                    leadingContent = {
                        SenIcon(
                            icon = menu.icon,
                            description = menu.iconDescription,
                            colors = SenIconDefaults.vibrantColors(menu.iconPalette),
                        )
                    },
                    onClick = { onNavigate(menu.destination) },
                ) {
                    Text(menu.label)
                }

                if (isLastShape and !isLastMenu) SenColumnSpacer()
            }
        }
    }
}

@SenPreviewCommon
@Composable
fun SenSettingsScreenPreview() {
    SenTheme {
        SenSettingsScreen(onNavigate = {})
    }
}
