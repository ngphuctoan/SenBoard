package banhmi.senboard.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LocalRainbowColors = staticCompositionLocalOf { LightRainbow }

val MaterialTheme.rainbow: RainbowColors
    @Composable
    @ReadOnlyComposable
    get() = LocalRainbowColors.current

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

/**
 * Extension to convert any [ColorScheme] to a truly neutral one by overriding 
 * tinted neutral slots with grayscale values while preserving accents.
 */
private fun ColorScheme.asNeutral(isDark: Boolean, isOled: Boolean): ColorScheme {
    return if (isDark) {
        val base = if (isOled) Gray0 else Gray10
        val elevated = if (isOled) Gray10 else Gray20
        val variant = if (isOled) Gray20 else Gray30

        this.copy(
            background = base,
            onBackground = Gray100,
            surface = base,
            onSurface = Gray100,
            surfaceVariant = variant,
            onSurfaceVariant = Gray70,
            surfaceTint = Color.Transparent,
            outline = Gray50,
            outlineVariant = elevated,
            surfaceContainerLowest = base,
            surfaceContainerLow = base,
            surfaceContainer = base,
            surfaceContainerHigh = elevated,
            surfaceContainerHighest = elevated,
            surfaceBright = elevated,
            surfaceDim = base,
            inverseSurface = Gray90,
            inverseOnSurface = Gray10
        )
    } else {
        this.copy(
            background = Gray100,
            onBackground = Gray0,
            surface = Gray100,
            onSurface = Gray0,
            surfaceVariant = Gray90,
            onSurfaceVariant = Gray30,
            surfaceTint = Color.Transparent,
            outline = Gray50,
            outlineVariant = Gray80,
            surfaceContainerLowest = Gray100,
            surfaceContainerLow = Gray100,
            surfaceContainer = Gray90,
            surfaceContainerHigh = Gray90,
            surfaceContainerHighest = Gray90,
            surfaceBright = Gray100,
            surfaceDim = Gray90,
            inverseSurface = Gray20,
            inverseOnSurface = Gray90
        )
    }
}

@Composable
fun SenBoardTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    neutral: Boolean = true,
    oled: Boolean = false,
    content: @Composable () -> Unit
) {
    val baseColorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val colorScheme = if (neutral) {
        baseColorScheme.asNeutral(darkTheme, oled)
    } else {
        baseColorScheme
    }

    val rainbowColors = if (darkTheme) DarkRainbow else LightRainbow

    CompositionLocalProvider(LocalRainbowColors provides rainbowColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content,
        )
    }
}