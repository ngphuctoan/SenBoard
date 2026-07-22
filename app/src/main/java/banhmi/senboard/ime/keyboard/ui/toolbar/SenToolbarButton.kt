package banhmi.senboard.ime.keyboard.ui.toolbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
private fun SenToolbarButtonArea(content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clickable(onClick = {}),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@Composable
private fun SenToolbarButtonSurface(
    color: Color,
    contentColor: Color,
    content: @Composable () -> Unit,
) {
    Surface(
        color = color,
        contentColor = contentColor,
        shape = CircleShape,
        modifier = Modifier.size(38.dp),
    ) {
        content()
    }
}

@Composable
private fun SenToolbarButtonContent(content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = Modifier
            .size(38.dp)
            .padding(7.dp),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@Composable
fun SenToolbarButton(
    color: Color = MaterialTheme.colorScheme.surfaceContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    content: @Composable () -> Unit,
) {
    SenToolbarButtonArea {
        SenToolbarButtonSurface(color, contentColor) {
            SenToolbarButtonContent {
                content()
            }
        }
    }
}
