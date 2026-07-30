package banhmi.senboard.app.settings.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class SenSettingsSpacerValues(
    val width: Dp,
    val height: Dp,
)

object SenSettingsSpacerDefaults {
    val DefaultSpacingValue: Dp = 16.dp

    val VerticalSpacingValues = SenSettingsSpacerValues(
        width = 0.dp, height = DefaultSpacingValue,
    )

    val HorizontalSpacingValues = SenSettingsSpacerValues(
        width = DefaultSpacingValue, height = 0.dp,
    )
}

@Composable
fun SenSettingsSpacer(
    modifier: Modifier = Modifier,
    spacingValues: SenSettingsSpacerValues = SenSettingsSpacerDefaults.VerticalSpacingValues,
) {
    Spacer(
        modifier = modifier
            .width(spacingValues.width)
            .height(spacingValues.height),
    )
}