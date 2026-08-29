package banhmi.senboard.keyboard.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

object SenToolbarDefaults {
    @Composable
    fun contentColor() = MaterialTheme.colorScheme.onSurface

    val HorizontalArrangement = Arrangement.Start

    val VerticalAlignment = Alignment.CenterVertically
}

@Composable
fun SenToolbar(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = SenToolbarDefaults.HorizontalArrangement,
    verticalAlignment: Alignment.Vertical = SenToolbarDefaults.VerticalAlignment,
    contentColor: Color = SenToolbarDefaults.contentColor(),
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment,
        modifier = modifier,
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            content()
        }
    }
}
