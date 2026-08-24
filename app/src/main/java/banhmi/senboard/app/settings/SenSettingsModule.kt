package banhmi.senboard.app.settings

import android.view.inputmethod.InputMethodManager
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PermDeviceInformation
import androidx.compose.material.icons.filled.Redeem
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import banhmi.senboard.BuildConfig
import banhmi.senboard.app.annotations.SenPreviewCommon
import banhmi.senboard.app.navigation.SenEntryProviderInstaller
import banhmi.senboard.app.navigation.SenNavigator
import banhmi.senboard.app.ui.SenColumn
import banhmi.senboard.app.ui.SenIcon
import banhmi.senboard.app.ui.SenIconDefaults
import banhmi.senboard.app.ui.SenMenu
import banhmi.senboard.app.ui.SenMenuDefaults
import banhmi.senboard.app.ui.SenScaffold
import banhmi.senboard.app.ui.lastSegmentedPadding
import banhmi.senboard.app.ui.segmentedPadding
import banhmi.senboard.data.preferences.SenPreferences
import banhmi.senboard.data.preferences.SenPreferencesViewModel
import banhmi.senboard.ui.theme.M3RefPalette
import banhmi.senboard.ui.theme.SenTheme
import banhmi.senboard.ui.theme.m3RefPaletteCyan
import banhmi.senboard.ui.theme.m3RefPaletteGreen
import banhmi.senboard.ui.theme.m3RefPalettePink
import banhmi.senboard.ui.theme.m3RefPaletteYellow
import banhmi.senboard.utils.IndexCount
import banhmi.senboard.utils.outOf
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
    fun provideEntryProviderInstaller(
        navigator: SenNavigator,
    ): SenEntryProviderInstaller = {
        entry<SenSettings> {
            SenSettingsScreen(navigator)
        }
    }
}

data class SenInputTesterColors(
    val color: Color,
    val outlineColor: Color,
)

object SenInputTesterDefaults {
    internal val BorderWidth = 1.dp

    internal val IndicatorColor = Color.Transparent

    val Shape = RoundedCornerShape(72.dp)

    val ContentPadding = PaddingValues(
        top = 24.dp,
        bottom = 24.dp,
        start = 20.dp,
        end = 16.dp,
    )

    @Composable
    fun textStyle() = LocalTextStyle.current.copy(fontSize = 20.sp)

    @Composable
    fun colors() = SenInputTesterColors(
        color = MaterialTheme.colorScheme.surface,
        outlineColor = MaterialTheme.colorScheme.outlineVariant,
    )
}

// Input tester doesn't need to pass in state or value/onValueChange, well it's meant for "testing" :D
@Composable
fun SenInputTester(
    modifier: Modifier = Modifier,
    leadingContent: @Composable (() -> Unit)? = null,
    shape: Shape = SenInputTesterDefaults.Shape,
    contentPadding: PaddingValues = SenInputTesterDefaults.ContentPadding,
    textStyle: TextStyle = SenInputTesterDefaults.textStyle(),
    colors: SenInputTesterColors = SenInputTesterDefaults.colors(),
) {
    TextField(
        state = rememberTextFieldState(),
        lineLimits = TextFieldLineLimits.SingleLine,
        placeholder = {
            Text(
                text = "Nhấp để gõ thử",
                style = textStyle,
            )
        },
        leadingIcon = leadingContent,
        textStyle = textStyle,
        shape = shape,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = colors.color,
            unfocusedContainerColor = colors.color,
            disabledContainerColor = colors.color,
            errorContainerColor = colors.color,
            focusedIndicatorColor = SenInputTesterDefaults.IndicatorColor,
            unfocusedIndicatorColor = SenInputTesterDefaults.IndicatorColor,
            disabledIndicatorColor = SenInputTesterDefaults.IndicatorColor,
            errorIndicatorColor = SenInputTesterDefaults.IndicatorColor,
        ),
        contentPadding = contentPadding,
        modifier = modifier.border(
            width = SenInputTesterDefaults.BorderWidth,
            color = colors.outlineColor,
            shape = shape,
        ),
    )
}

object SenKeyboardSwitcherDefaults {
    internal val Padding = PaddingValues(start = 12.dp)
}

@Composable
fun SenKeyboardSwitcher(
    modifier: Modifier = Modifier,
    onKeyboardSwitching: () -> Unit,
) {
    IconButton(
        onClick = onKeyboardSwitching,
        modifier = modifier,
    ) {
        Icon(
            imageVector = Icons.Outlined.MoreVert,
            contentDescription = "Đổi phương thức nhập",
        )
    }
}

data class SenSettingsMenu(
    val indexCount: IndexCount,
    val destination: Any,
    val label: String,
    val supportingLabel: String,
    val icon: ImageVector,
    val iconPalette: M3RefPalette,
    val iconDescription: String?,
)

// This is useful conditional rendering using plain if-else statements!
class SenSettingsMenusScope {
    private val menus: MutableList<SenSettingsMenu> = mutableListOf()

    fun senSettingsMenu(
        indexCount: IndexCount,
        destination: Any,
        label: String,
        supportingLabel: String,
        icon: ImageVector,
        iconPalette: M3RefPalette,
        iconDescription: String? = label,
    ) = menus.add(
        SenSettingsMenu(
            indexCount = indexCount,
            destination = destination,
            label = label,
            supportingLabel = supportingLabel,
            icon = icon,
            iconPalette = iconPalette,
            iconDescription = iconDescription,
        ),
    )

    fun build() = menus.toList()
}

fun senSettingsMenus(
    builder: SenSettingsMenusScope.() -> Unit,
) = SenSettingsMenusScope().apply(builder).build()

object SenSettingsDefaults {
    internal val TopBarPaddings = PaddingValues(
        top = 0.dp,
        bottom = 8.dp,
        start = 16.dp,
        end = 16.dp,
    )

    internal fun menus(
        easterEggsEnabled: Boolean,
        developerOptionsEnabled: Boolean,
    ) = senSettingsMenus {
        senSettingsMenu(
            indexCount = 0 outOf if (developerOptionsEnabled) 3 else 2,
            destination = SenHelp,
            label = "Hướng dẫn sử dụng",
            supportingLabel = "Chữ Việt song song",
            icon = Icons.AutoMirrored.Filled.MenuBook,
            iconPalette = m3RefPaletteCyan,
        )
        senSettingsMenu(
            indexCount = 1 outOf if (developerOptionsEnabled) 3 else 2,
            destination = SenAbout,
            label = "Giới thiệu ứng dụng",
            supportingLabel = BuildConfig.VERSION_NAME,
            icon = Icons.Filled.PermDeviceInformation,
            iconPalette = m3RefPaletteCyan,
        )
        if (developerOptionsEnabled) {
            senSettingsMenu(
                indexCount = 2 outOf 3,
                destination = SenDeveloperOptions,
                label = "Chế độ nhà phát triển",
                supportingLabel = BuildConfig.APPLICATION_ID, // Placeholder
                icon = Icons.Filled.Code,
                iconPalette = m3RefPaletteCyan,
            )
        }
        senSettingsMenu(
            indexCount = 0 outOf if (easterEggsEnabled) 4 else 3,
            destination = SenInputMethod,
            label = "Phương thức nhập",
            supportingLabel = "Tự động viết hoa, phím tắt, gợi ý",
            icon = Icons.Filled.Keyboard,
            iconPalette = m3RefPalettePink,
        )
        senSettingsMenu(
            indexCount = 1 outOf if (easterEggsEnabled) 4 else 3,
            destination = SenAppearance,
            label = "Giao diện",
            supportingLabel = "Nền phím, đổ bóng nền",
            icon = Icons.Filled.Palette,
            iconPalette = m3RefPalettePink,
        )
        senSettingsMenu(
            indexCount = 2 outOf if (easterEggsEnabled) 4 else 3,
            destination = SenHaptics,
            label = "Haptic",
            supportingLabel = "Độ mạnh của haptic",
            icon = Icons.Filled.Vibration,
            iconPalette = m3RefPaletteYellow,
        )
        if (easterEggsEnabled) {
            senSettingsMenu(
                indexCount = 3 outOf 4,
                destination = SenEasterEggs,
                label = "Easter Egg",
                supportingLabel = "Chế độ aaaaa",
                icon = Icons.Filled.Redeem,
                iconPalette = m3RefPaletteGreen,
            )
        }
    }
}

@Composable
fun SenSettingsScreen(
    navigator: SenNavigator,
    preferencesViewModel: SenPreferencesViewModel = hiltViewModel(),
) {
    val preferences by preferencesViewModel.preferences.collectAsStateWithLifecycle()

    SenSettingsContent(
        onNavigate = navigator::goTo,
        easterEggsEnabled = preferences.easterEggsEnabled,
        developerOptionsEnabled = preferences.developerOptionsEnabled,
    )
}

@Composable
fun SenSettingsContent(
    onNavigate: (Any) -> Unit = {},
    easterEggsEnabled: Boolean,
    developerOptionsEnabled: Boolean,
) {
    val context = LocalContext.current

    val imService = ContextCompat.getSystemService(context, InputMethodManager::class.java)

    val menus = SenSettingsDefaults.menus(easterEggsEnabled, developerOptionsEnabled)

    SenScaffold(
        topBar = {
            SenInputTester(
                leadingContent = {
                    SenKeyboardSwitcher(
                        onKeyboardSwitching = { imService?.showInputMethodPicker() },
                        modifier = Modifier.padding(SenKeyboardSwitcherDefaults.Padding),
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(SenSettingsDefaults.TopBarPaddings),
            )
        },
    ) { innerPadding ->
        SenColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
        ) {
            itemsIndexed(menus) { index, menu ->
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
                    modifier = if (menu.indexCount.isLast() && index != menus.lastIndex) {
                        Modifier.lastSegmentedPadding()
                    } else {
                        Modifier.segmentedPadding()
                    },
                ) {
                    Text(menu.label)
                }
            }
        }
    }
}

@SenPreviewCommon
@Composable
fun SenSettingsScreenPreview() {
    // Enable Easter Eggs and Developer Options to show all menus in preview
    var preferences by remember {
        mutableStateOf(
            SenPreferences(
                easterEggsEnabled = true,
                developerOptionsEnabled = true,
            ),
        )
    }

    SenTheme {
        SenSettingsContent(
            easterEggsEnabled = preferences.easterEggsEnabled,
            developerOptionsEnabled = preferences.developerOptionsEnabled,
        )
    }
}
