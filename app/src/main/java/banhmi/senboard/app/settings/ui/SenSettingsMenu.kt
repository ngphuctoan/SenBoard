package banhmi.senboard.app.settings.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ListItemShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

object SenSettingsMenuDefaults {
    @Composable
    fun colors(): ListItemColors = ListItemDefaults.segmentedColors(
        containerColor = MaterialTheme.colorScheme.surfaceBright,
        disabledContainerColor = MaterialTheme.colorScheme.surfaceBright,
    )

    val ContentPadding: PaddingValues = PaddingValues(horizontal = 16.dp)
}

@Composable
fun SenSettingsMenu(
    shapes: ListItemShapes,
    headlineContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    supportingContent: @Composable (() -> Unit)? = null,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    contentPadding: PaddingValues = SenSettingsMenuDefaults.ContentPadding,
    colors: ListItemColors = SenSettingsMenuDefaults.colors(),
    onClick: (() -> Unit)? = null,
) {
    val clickableModifier = if (onClick != null) modifier.clickable(onClick = onClick) else modifier

    /* I don't like wrapping things inside a "container" wrapper, especially a Box, but this is
    the only way to have an outer padding but still make the interaction full-width afaik */
    Box(modifier = clickableModifier.padding(contentPadding)) {
        SegmentedListItem(
            shapes = shapes,
            colors = colors,
            content = headlineContent,
            supportingContent = supportingContent,
            leadingContent = leadingContent,
            trailingContent = trailingContent,
        )
    }
}
