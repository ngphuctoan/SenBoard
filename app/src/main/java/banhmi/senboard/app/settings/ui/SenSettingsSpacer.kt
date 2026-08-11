package banhmi.senboard.app.settings.ui

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Technically not an actual @Composable function, but this is a much better abstraction

object SenSettingsSpacerDefaults {
    val HorizontalSpacing: Dp = 16.dp

    val VerticalSpacing: Dp = 16.dp
}

fun Modifier.senSettingsHorizontalSpacer(spacing: Dp = SenSettingsSpacerDefaults.HorizontalSpacing) =
    width(spacing).fillMaxHeight()

fun Modifier.senSettingsVerticalSpacer(spacing: Dp = SenSettingsSpacerDefaults.VerticalSpacing) =
    height(spacing).fillMaxWidth()
