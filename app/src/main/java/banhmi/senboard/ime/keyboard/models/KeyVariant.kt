package banhmi.senboard.ime.keyboard.models

import banhmi.senboard.ime.keyboard.core.SenBoardContext
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import banhmi.senboard.shared.settings.SenSettingsViewModel

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
    Neutral, Ghost, Tertiary, Secondary, Primary,
}

@Composable
operator fun KeyVariant.invoke(context: SenBoardContext): KeyStyle {
    val appearanceState by context.appearanceStateFlow.collectAsStateWithLifecycle()

    val ghostKeyStyle = KeyStyle(
        color = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurface,
        useReferenceFontSize = true,
    )

    return when (this) {
        KeyVariant.Neutral -> if (appearanceState.showKeyBackground) KeyStyle(
            color = MaterialTheme.colorScheme.surfaceBright,
            contentColor = MaterialTheme.colorScheme.onSurface,
            useReferenceFontSize = true,
        ) else ghostKeyStyle

        KeyVariant.Ghost -> ghostKeyStyle

        KeyVariant.Tertiary -> KeyStyle(
            color = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
            fontWeight = FontWeight.Medium,
            maxFontSize = 24.sp,
        )

        KeyVariant.Secondary -> if (appearanceState.showKeyBackground) KeyStyle(
            color = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            useReferenceFontSize = true,
        ) else ghostKeyStyle

        KeyVariant.Primary -> KeyStyle(
            color = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.Medium,
            maxFontSize = 24.sp,
        )
    }
}
