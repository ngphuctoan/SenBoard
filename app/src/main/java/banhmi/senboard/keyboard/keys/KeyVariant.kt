package banhmi.senboard.keyboard.keys

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class KeyVariant {
    Neutral, NeutralRaised, Primary,
}

@Immutable
data class KeyTypography(
    val label: TextStyle = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Normal,
        fontSize = 40.sp,
    ),
    val supportLabel: TextStyle = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
    ),
)

@Immutable
data class IconSizes(
    val main: Dp  = 32.dp,
    val support: Dp = 20.dp,
)

data class KeyStyle(
    val color: Color,
    val contentColor: Color,
    val shape: Shape = RoundedCornerShape(8.dp),
    val typography: KeyTypography = KeyTypography(),
    val iconSizes: IconSizes = IconSizes(),
)

@Composable
fun provideKeyStyle(variant: KeyVariant): KeyStyle {
    return when (variant) {
        KeyVariant.Neutral -> KeyStyle(
            color = MaterialTheme.colorScheme.surfaceContainerHighest,
            contentColor = MaterialTheme.colorScheme.onSurface,
        )

        KeyVariant.NeutralRaised -> KeyStyle(
            color = MaterialTheme.colorScheme.outlineVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            typography = KeyTypography(label = TextStyle(
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Normal,
                fontSize = 32.sp,
            )),
        )

        KeyVariant.Primary -> KeyStyle(
            color = MaterialTheme.colorScheme.inversePrimary,
            contentColor = MaterialTheme.colorScheme.primary,
            shape = CircleShape,
        )
    }
}
