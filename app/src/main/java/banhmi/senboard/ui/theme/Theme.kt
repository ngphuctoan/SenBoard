package banhmi.senboard.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

private val mediumContrastLightColorScheme = lightColorScheme(
    primary = primaryLightMediumContrast,
    onPrimary = onPrimaryLightMediumContrast,
    primaryContainer = primaryContainerLightMediumContrast,
    onPrimaryContainer = onPrimaryContainerLightMediumContrast,
    secondary = secondaryLightMediumContrast,
    onSecondary = onSecondaryLightMediumContrast,
    secondaryContainer = secondaryContainerLightMediumContrast,
    onSecondaryContainer = onSecondaryContainerLightMediumContrast,
    tertiary = tertiaryLightMediumContrast,
    onTertiary = onTertiaryLightMediumContrast,
    tertiaryContainer = tertiaryContainerLightMediumContrast,
    onTertiaryContainer = onTertiaryContainerLightMediumContrast,
    error = errorLightMediumContrast,
    onError = onErrorLightMediumContrast,
    errorContainer = errorContainerLightMediumContrast,
    onErrorContainer = onErrorContainerLightMediumContrast,
    background = backgroundLightMediumContrast,
    onBackground = onBackgroundLightMediumContrast,
    surface = surfaceLightMediumContrast,
    onSurface = onSurfaceLightMediumContrast,
    surfaceVariant = surfaceVariantLightMediumContrast,
    onSurfaceVariant = onSurfaceVariantLightMediumContrast,
    outline = outlineLightMediumContrast,
    outlineVariant = outlineVariantLightMediumContrast,
    scrim = scrimLightMediumContrast,
    inverseSurface = inverseSurfaceLightMediumContrast,
    inverseOnSurface = inverseOnSurfaceLightMediumContrast,
    inversePrimary = inversePrimaryLightMediumContrast,
    surfaceDim = surfaceDimLightMediumContrast,
    surfaceBright = surfaceBrightLightMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestLightMediumContrast,
    surfaceContainerLow = surfaceContainerLowLightMediumContrast,
    surfaceContainer = surfaceContainerLightMediumContrast,
    surfaceContainerHigh = surfaceContainerHighLightMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestLightMediumContrast,
)

private val highContrastLightColorScheme = lightColorScheme(
    primary = primaryLightHighContrast,
    onPrimary = onPrimaryLightHighContrast,
    primaryContainer = primaryContainerLightHighContrast,
    onPrimaryContainer = onPrimaryContainerLightHighContrast,
    secondary = secondaryLightHighContrast,
    onSecondary = onSecondaryLightHighContrast,
    secondaryContainer = secondaryContainerLightHighContrast,
    onSecondaryContainer = onSecondaryContainerLightHighContrast,
    tertiary = tertiaryLightHighContrast,
    onTertiary = onTertiaryLightHighContrast,
    tertiaryContainer = tertiaryContainerLightHighContrast,
    onTertiaryContainer = onTertiaryContainerLightHighContrast,
    error = errorLightHighContrast,
    onError = onErrorLightHighContrast,
    errorContainer = errorContainerLightHighContrast,
    onErrorContainer = onErrorContainerLightHighContrast,
    background = backgroundLightHighContrast,
    onBackground = onBackgroundLightHighContrast,
    surface = surfaceLightHighContrast,
    onSurface = onSurfaceLightHighContrast,
    surfaceVariant = surfaceVariantLightHighContrast,
    onSurfaceVariant = onSurfaceVariantLightHighContrast,
    outline = outlineLightHighContrast,
    outlineVariant = outlineVariantLightHighContrast,
    scrim = scrimLightHighContrast,
    inverseSurface = inverseSurfaceLightHighContrast,
    inverseOnSurface = inverseOnSurfaceLightHighContrast,
    inversePrimary = inversePrimaryLightHighContrast,
    surfaceDim = surfaceDimLightHighContrast,
    surfaceBright = surfaceBrightLightHighContrast,
    surfaceContainerLowest = surfaceContainerLowestLightHighContrast,
    surfaceContainerLow = surfaceContainerLowLightHighContrast,
    surfaceContainer = surfaceContainerLightHighContrast,
    surfaceContainerHigh = surfaceContainerHighLightHighContrast,
    surfaceContainerHighest = surfaceContainerHighestLightHighContrast,
)

private val mediumContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkMediumContrast,
    onPrimary = onPrimaryDarkMediumContrast,
    primaryContainer = primaryContainerDarkMediumContrast,
    onPrimaryContainer = onPrimaryContainerDarkMediumContrast,
    secondary = secondaryDarkMediumContrast,
    onSecondary = onSecondaryDarkMediumContrast,
    secondaryContainer = secondaryContainerDarkMediumContrast,
    onSecondaryContainer = onSecondaryContainerDarkMediumContrast,
    tertiary = tertiaryDarkMediumContrast,
    onTertiary = onTertiaryDarkMediumContrast,
    tertiaryContainer = tertiaryContainerDarkMediumContrast,
    onTertiaryContainer = onTertiaryContainerDarkMediumContrast,
    error = errorDarkMediumContrast,
    onError = onErrorDarkMediumContrast,
    errorContainer = errorContainerDarkMediumContrast,
    onErrorContainer = onErrorContainerDarkMediumContrast,
    background = backgroundDarkMediumContrast,
    onBackground = onBackgroundDarkMediumContrast,
    surface = surfaceDarkMediumContrast,
    onSurface = onSurfaceDarkMediumContrast,
    surfaceVariant = surfaceVariantDarkMediumContrast,
    onSurfaceVariant = onSurfaceVariantDarkMediumContrast,
    outline = outlineDarkMediumContrast,
    outlineVariant = outlineVariantDarkMediumContrast,
    scrim = scrimDarkMediumContrast,
    inverseSurface = inverseSurfaceDarkMediumContrast,
    inverseOnSurface = inverseOnSurfaceDarkMediumContrast,
    inversePrimary = inversePrimaryDarkMediumContrast,
    surfaceDim = surfaceDimDarkMediumContrast,
    surfaceBright = surfaceBrightDarkMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkMediumContrast,
    surfaceContainerLow = surfaceContainerLowDarkMediumContrast,
    surfaceContainer = surfaceContainerDarkMediumContrast,
    surfaceContainerHigh = surfaceContainerHighDarkMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkMediumContrast,
)

private val highContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkHighContrast,
    onPrimary = onPrimaryDarkHighContrast,
    primaryContainer = primaryContainerDarkHighContrast,
    onPrimaryContainer = onPrimaryContainerDarkHighContrast,
    secondary = secondaryDarkHighContrast,
    onSecondary = onSecondaryDarkHighContrast,
    secondaryContainer = secondaryContainerDarkHighContrast,
    onSecondaryContainer = onSecondaryContainerDarkHighContrast,
    tertiary = tertiaryDarkHighContrast,
    onTertiary = onTertiaryDarkHighContrast,
    tertiaryContainer = tertiaryContainerDarkHighContrast,
    onTertiaryContainer = onTertiaryContainerDarkHighContrast,
    error = errorDarkHighContrast,
    onError = onErrorDarkHighContrast,
    errorContainer = errorContainerDarkHighContrast,
    onErrorContainer = onErrorContainerDarkHighContrast,
    background = backgroundDarkHighContrast,
    onBackground = onBackgroundDarkHighContrast,
    surface = surfaceDarkHighContrast,
    onSurface = onSurfaceDarkHighContrast,
    surfaceVariant = surfaceVariantDarkHighContrast,
    onSurfaceVariant = onSurfaceVariantDarkHighContrast,
    outline = outlineDarkHighContrast,
    outlineVariant = outlineVariantDarkHighContrast,
    scrim = scrimDarkHighContrast,
    inverseSurface = inverseSurfaceDarkHighContrast,
    inverseOnSurface = inverseOnSurfaceDarkHighContrast,
    inversePrimary = inversePrimaryDarkHighContrast,
    surfaceDim = surfaceDimDarkHighContrast,
    surfaceBright = surfaceBrightDarkHighContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkHighContrast,
    surfaceContainerLow = surfaceContainerLowDarkHighContrast,
    surfaceContainer = surfaceContainerDarkHighContrast,
    surfaceContainerHigh = surfaceContainerHighDarkHighContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkHighContrast,
)

@Immutable
data class ColorFamily(
    val color: Color, val onColor: Color, val colorContainer: Color, val onColorContainer: Color
)

val unspecified_scheme = ColorFamily(
    Color.Unspecified, Color.Unspecified, Color.Unspecified, Color.Unspecified
)

@Immutable
data class M3RefPalette(
    val color100: Color,
    val color98: Color,
    val color95: Color,
    val color90: Color,
    val color80: Color,
    val color70: Color,
    val color60: Color,
    val color50: Color,
    val color40: Color,
    val color30: Color,
    val color20: Color,
    val color10: Color,
    val color0: Color,
)

val m3RefPaletteBlue = M3RefPalette(
    color100 = m3RefPaletteBlue100,
    color98 = m3RefPaletteBlue98,
    color95 = m3RefPaletteBlue95,
    color90 = m3RefPaletteBlue90,
    color80 = m3RefPaletteBlue80,
    color70 = m3RefPaletteBlue70,
    color60 = m3RefPaletteBlue60,
    color50 = m3RefPaletteBlue50,
    color40 = m3RefPaletteBlue40,
    color30 = m3RefPaletteBlue30,
    color20 = m3RefPaletteBlue20,
    color10 = m3RefPaletteBlue10,
    color0 = m3RefPaletteBlue0,
)

val m3RefPaletteYellow = M3RefPalette(
    color100 = m3RefPaletteYellow100,
    color98 = m3RefPaletteYellow98,
    color95 = m3RefPaletteYellow95,
    color90 = m3RefPaletteYellow90,
    color80 = m3RefPaletteYellow80,
    color70 = m3RefPaletteYellow70,
    color60 = m3RefPaletteYellow60,
    color50 = m3RefPaletteYellow50,
    color40 = m3RefPaletteYellow40,
    color30 = m3RefPaletteYellow30,
    color20 = m3RefPaletteYellow20,
    color10 = m3RefPaletteYellow10,
    color0 = m3RefPaletteYellow0,
)

val m3RefPaletteRed = M3RefPalette(
    color100 = m3RefPaletteRed100,
    color98 = m3RefPaletteRed98,
    color95 = m3RefPaletteRed95,
    color90 = m3RefPaletteRed90,
    color80 = m3RefPaletteRed80,
    color70 = m3RefPaletteRed70,
    color60 = m3RefPaletteRed60,
    color50 = m3RefPaletteRed50,
    color40 = m3RefPaletteRed40,
    color30 = m3RefPaletteRed30,
    color20 = m3RefPaletteRed20,
    color10 = m3RefPaletteRed10,
    color0 = m3RefPaletteRed0,
)

val m3RefPalettePurple = M3RefPalette(
    color100 = m3RefPalettePurple100,
    color98 = m3RefPalettePurple98,
    color95 = m3RefPalettePurple95,
    color90 = m3RefPalettePurple90,
    color80 = m3RefPalettePurple80,
    color70 = m3RefPalettePurple70,
    color60 = m3RefPalettePurple60,
    color50 = m3RefPalettePurple50,
    color40 = m3RefPalettePurple40,
    color30 = m3RefPalettePurple30,
    color20 = m3RefPalettePurple20,
    color10 = m3RefPalettePurple10,
    color0 = m3RefPalettePurple0,
)

val m3RefPaletteBlueVariant = M3RefPalette(
    color100 = m3RefPaletteBlueVariant100,
    color98 = m3RefPaletteBlueVariant98,
    color95 = m3RefPaletteBlueVariant95,
    color90 = m3RefPaletteBlueVariant90,
    color80 = m3RefPaletteBlueVariant80,
    color70 = m3RefPaletteBlueVariant70,
    color60 = m3RefPaletteBlueVariant60,
    color50 = m3RefPaletteBlueVariant50,
    color40 = m3RefPaletteBlueVariant40,
    color30 = m3RefPaletteBlueVariant30,
    color20 = m3RefPaletteBlueVariant20,
    color10 = m3RefPaletteBlueVariant10,
    color0 = m3RefPaletteBlueVariant0,
)

val m3RefPaletteCyan = M3RefPalette(
    color100 = m3RefPaletteCyan100,
    color98 = m3RefPaletteCyan98,
    color95 = m3RefPaletteCyan95,
    color90 = m3RefPaletteCyan90,
    color80 = m3RefPaletteCyan80,
    color70 = m3RefPaletteCyan70,
    color60 = m3RefPaletteCyan60,
    color50 = m3RefPaletteCyan50,
    color40 = m3RefPaletteCyan40,
    color30 = m3RefPaletteCyan30,
    color20 = m3RefPaletteCyan20,
    color10 = m3RefPaletteCyan10,
    color0 = m3RefPaletteCyan0,
)

val m3RefPaletteGrey = M3RefPalette(
    color100 = m3RefPaletteGrey100,
    color98 = m3RefPaletteGrey98,
    color95 = m3RefPaletteGrey95,
    color90 = m3RefPaletteGrey90,
    color80 = m3RefPaletteGrey80,
    color70 = m3RefPaletteGrey70,
    color60 = m3RefPaletteGrey60,
    color50 = m3RefPaletteGrey50,
    color40 = m3RefPaletteGrey40,
    color30 = m3RefPaletteGrey30,
    color20 = m3RefPaletteGrey20,
    color10 = m3RefPaletteGrey10,
    color0 = m3RefPaletteGrey0,
)

val m3RefPaletteGreen = M3RefPalette(
    color100 = m3RefPaletteGreen100,
    color98 = m3RefPaletteGreen98,
    color95 = m3RefPaletteGreen95,
    color90 = m3RefPaletteGreen90,
    color80 = m3RefPaletteGreen80,
    color70 = m3RefPaletteGreen70,
    color60 = m3RefPaletteGreen60,
    color50 = m3RefPaletteGreen50,
    color40 = m3RefPaletteGreen40,
    color30 = m3RefPaletteGreen30,
    color20 = m3RefPaletteGreen20,
    color10 = m3RefPaletteGreen10,
    color0 = m3RefPaletteGreen0,
)

val m3RefPaletteGreyVariant = M3RefPalette(
    color100 = m3RefPaletteGreyVariant100,
    color98 = m3RefPaletteGreyVariant98,
    color95 = m3RefPaletteGreyVariant95,
    color90 = m3RefPaletteGreyVariant90,
    color80 = m3RefPaletteGreyVariant80,
    color70 = m3RefPaletteGreyVariant70,
    color60 = m3RefPaletteGreyVariant60,
    color50 = m3RefPaletteGreyVariant50,
    color40 = m3RefPaletteGreyVariant40,
    color30 = m3RefPaletteGreyVariant30,
    color20 = m3RefPaletteGreyVariant20,
    color10 = m3RefPaletteGreyVariant10,
    color0 = m3RefPaletteGreyVariant0,
)

val m3RefPaletteOrange = M3RefPalette(
    color100 = m3RefPaletteOrange100,
    color98 = m3RefPaletteOrange98,
    color95 = m3RefPaletteOrange95,
    color90 = m3RefPaletteOrange90,
    color80 = m3RefPaletteOrange80,
    color70 = m3RefPaletteOrange70,
    color60 = m3RefPaletteOrange60,
    color50 = m3RefPaletteOrange50,
    color40 = m3RefPaletteOrange40,
    color30 = m3RefPaletteOrange30,
    color20 = m3RefPaletteOrange20,
    color10 = m3RefPaletteOrange10,
    color0 = m3RefPaletteOrange0,
)

val m3RefPalettePink = M3RefPalette(
    color100 = m3RefPalettePink100,
    color98 = m3RefPalettePink98,
    color95 = m3RefPalettePink95,
    color90 = m3RefPalettePink90,
    color80 = m3RefPalettePink80,
    color70 = m3RefPalettePink70,
    color60 = m3RefPalettePink60,
    color50 = m3RefPalettePink50,
    color40 = m3RefPalettePink40,
    color30 = m3RefPalettePink30,
    color20 = m3RefPalettePink20,
    color10 = m3RefPalettePink10,
    color0 = m3RefPalettePink0,
)

@Composable
fun SenBoardTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true, content: @Composable() () -> Unit
) {
    val isSnowCone = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val isOneUi = Build.BRAND.lowercase() == "samsung"

    val colorScheme = when {
        /* One UI does not respect the contrast between surface and surface container,
        so for Samsung devices the app will use the default monochrome theme */
        dynamicColor && isSnowCone && !isOneUi -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkScheme
        else -> lightScheme
    }

    MaterialTheme(
        colorScheme = colorScheme, typography = AppTypography, content = content
    )
}

