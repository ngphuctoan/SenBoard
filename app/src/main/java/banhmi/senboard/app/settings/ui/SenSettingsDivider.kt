package banhmi.senboard.app.settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import banhmi.senboard.app.settings.ui.scope.SenSettingsListGroupScope

@Composable
fun SenSettingsListGroupScope.SenSettingsDivider() {
    val iconSize = if (isSubMenu) iconSize else iconShapeSize
    val additionalPadding = if (showIcons) iconSize + horizontalPadding else 0.dp

    Box(
        modifier = Modifier
            .padding(
                start = horizontalMargin + horizontalPadding + additionalPadding + 32.dp,
                end = horizontalMargin + 16.dp,
            )
            .fillMaxWidth()
            .height(1.5.dp)
            .background(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                shape = CircleShape,
            )
    )
}
