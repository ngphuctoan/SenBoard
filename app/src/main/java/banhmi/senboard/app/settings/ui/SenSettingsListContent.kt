package banhmi.senboard.app.settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import banhmi.senboard.app.settings.ui.scope.SenSettingsListGroupScope

@Composable
private fun BoxScope.SenSettingsListBackground(horizontalMargin: Dp, radius: Dp) {
    Box(
        modifier = Modifier
            .matchParentSize()
            .padding(horizontal = horizontalMargin)
            .background(
                color = MaterialTheme.colorScheme.surfaceBright,
                shape = RoundedCornerShape(radius),
            ),
    )
}

@Composable
fun SenSettingsListGroupScope.SenSettingsListContent(content: @Composable SenSettingsListGroupScope.() -> Unit) {
    Box {
        SenSettingsListBackground(horizontalMargin, containerRadius)
        Column {
            this@SenSettingsListContent.content()
        }
    }
}
