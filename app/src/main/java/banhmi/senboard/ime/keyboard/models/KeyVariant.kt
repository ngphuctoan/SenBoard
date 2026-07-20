package banhmi.senboard.ime.keyboard.models

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

data class KeyStyle(
    val color: Color,
    val contentColor: Color,
    val shape: Shape = RoundedCornerShape(8.dp),
    val fontWeight: FontWeight = FontWeight.Normal,
    val minFontSize: TextUnit = 12.sp,
    val maxFontSize: TextUnit = 32.sp,
    val iconSize: Dp = 32.dp,
    val useReferenceFontSize: Boolean = false,
    val contentPadding: Dp = 8.dp,
)

enum class KeyVariant {
    Neutral,
    Ghost,
    Tertiary,
    Secondary,
    Primary,
}

@Composable
operator fun KeyVariant.invoke(): KeyStyle =
    when (this) {
        KeyVariant.Neutral -> KeyStyle(
            color = MaterialTheme.colorScheme.surfaceBright,
            contentColor = MaterialTheme.colorScheme.onSurface,
            useReferenceFontSize = true,
        )

        KeyVariant.Ghost -> KeyStyle(
            color = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSurface,
            useReferenceFontSize = true,
        )

        KeyVariant.Tertiary -> KeyStyle(
            color = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
            shape = CircleShape,
            fontWeight = FontWeight.Medium,
            maxFontSize = 24.sp,
        )

        KeyVariant.Secondary -> KeyStyle(
            color = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            useReferenceFontSize = true,
        )

        KeyVariant.Primary -> KeyStyle(
            color = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            shape = CircleShape,
            fontWeight = FontWeight.Medium,
            maxFontSize = 24.sp,
        )
    }
