package banhmi.senboard.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import banhmi.senboard.ui.theme.M3RefPalette
import banhmi.senboard.ui.theme.SenTheme
import banhmi.senboard.ui.theme.m3RefPaletteBlue
import banhmi.senboard.ui.theme.m3RefPaletteGreen
import banhmi.senboard.ui.theme.m3RefPaletteYellow

data class SenIconSizes(
    val size: Dp,
    val contentSize: Dp,
)

data class SenIconColors(
    val color: Color,
    val contentColor: Color,
)

object SenIconDefaults {
    val Shape = CircleShape

    @Composable
    fun shape() : Shape = appIconShape()

    val Sizes = SenIconSizes(
        size = 40.dp,
        contentSize = 24.dp,
    )

    @Composable
    fun colors() = SenIconColors(
        color = Color.Transparent,
        contentColor = LocalContentColor.current,
    )

    // Vibrant colors based on the Material 3 reference palette and scales from AOSP settings
    @Composable
    fun vibrantColors(palette: M3RefPalette, darkTheme: Boolean = isSystemInDarkTheme()) =
        SenIconColors(
            color = if (darkTheme) palette.color80 else palette.color90,
            contentColor = palette.color30,
        )

}

@Composable
fun SenIcon(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    description: String? = null,
    shape: Shape = SenIconDefaults.shape(),
    sizes: SenIconSizes = SenIconDefaults.Sizes,
    colors: SenIconColors = SenIconDefaults.colors(),
) {
    val paddingValues = remember(sizes) {
        // Prevents shape size being smaller than icon size
        PaddingValues(
            (sizes.size - sizes.contentSize)
                .div(2)
                .coerceAtLeast(0.dp)
        )
    }

    Icon(
        imageVector = icon,
        contentDescription = description,
        tint = colors.contentColor,
        modifier = modifier
            .background(
                shape = shape,
                color = colors.color,
            )
            .size(sizes.size)
            .padding(paddingValues),
    )
}

@Composable
@PreviewLightDark
fun SenIconPreview() {
    val icons: Map<ImageVector, M3RefPalette> = mapOf(
        Icons.Filled.Home to m3RefPaletteBlue,
        Icons.Filled.Explore to m3RefPaletteGreen,
        Icons.Filled.Settings to m3RefPaletteYellow,
    )

    SenTheme {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(8.dp),
        ) {
            icons.forEach { (icon, palette) ->
                SenIcon(icon = icon, colors = SenIconDefaults.vibrantColors(palette))
            }
        }
    }
}
