package banhmi.senboard.app.settings.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import banhmi.senboard.ui.theme.M3RefPalette

data class SenSettingsIconSizes(
    val iconSize: Dp,
    val shapeSize: Dp,
)

data class SenSettingsIconColors(
    val iconColor: Color,
    val shapeColor: Color,
)

object SenSettingsIconDefaults {
    // Vibrant colors based on the Material 3 reference palette and scales from AOSP settings
    @Composable
    fun vibrantColors(
        palette: M3RefPalette,
        darkTheme: Boolean = isSystemInDarkTheme(),
    ) = SenSettingsIconColors(
        iconColor = palette.color30,
        if (darkTheme) palette.color80 else palette.color90,
    )

    @Composable
    fun neutralColors() = SenSettingsIconColors(
        iconColor = LocalContentColor.current,
        shapeColor = Color.Transparent,
    )

    fun iconOnlySizes(baseSize: Dp = 24.dp) = SenSettingsIconSizes(
        iconSize = baseSize,
        shapeSize = baseSize,
    )

    val IconWithShapeSizes = SenSettingsIconSizes(
        iconSize = 24.dp,
        shapeSize = 40.dp,
    )

    val ContainerShape = CircleShape
}

@Composable
fun SenSettingsIcon(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    description: String? = null,
    sizes: SenSettingsIconSizes = SenSettingsIconDefaults.iconOnlySizes(),
    colors: SenSettingsIconColors = SenSettingsIconDefaults.neutralColors(),
    containerShape: Shape = SenSettingsIconDefaults.ContainerShape,
) {
    Surface(
        color = colors.shapeColor,
        contentColor = colors.iconColor,
        shape = containerShape,
        modifier = modifier,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = description,
            modifier = Modifier
                // Prevents shape size being smaller than icon size
                .padding((sizes.shapeSize - sizes.iconSize).div(2).coerceAtLeast(0.dp))
                .size(sizes.iconSize),
        )
    }
}
