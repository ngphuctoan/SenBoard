package banhmi.senboard.app.settings.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ListItemShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object SenSettingsMenuDefaults {
    @Composable
    fun neutralColors(): ListItemColors = ListItemDefaults.segmentedColors(
        containerColor = MaterialTheme.colorScheme.surfaceBright,
        disabledContainerColor = MaterialTheme.colorScheme.surfaceBright,
    )

    @Composable
    fun primaryContainerColors(): ListItemColors = ListItemDefaults.segmentedColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        disabledContainerColor = MaterialTheme.colorScheme.surfaceBright,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        supportingContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
    )

    /* Default (Segmented)ListItem when there is only a one-line supporting content
    has a smaller height than two-line supporting content */
    val MinHeight: Dp = 72.dp

    // The padding between screen and actual list item, meant to be applied for the Box container
    val OuterPadding: PaddingValues = PaddingValues(horizontal = 16.dp)

    val ContentPadding: PaddingValues = PaddingValues(16.dp)

    @Composable
    fun pillShapes(): ListItemShapes = ListItemDefaults.shapes(CircleShape)

    val PillOuterPadding: PaddingValues = PaddingValues(16.dp)

    val PillContentPadding: PaddingValues = PaddingValues(
        top = 16.dp, bottom = 16.dp, start = 32.dp, end = 16.dp,
    )
}

object SenSettingsMenuExtraDefaults {
    val Padding: PaddingValues = PaddingValues(horizontal = 8.dp)
}

@Composable
fun SenSettingsMenu(
    shapes: ListItemShapes,
    headlineContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    supportingContent: @Composable (() -> Unit)? = null,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    minHeight: Dp = SenSettingsMenuDefaults.MinHeight,
    outerPadding: PaddingValues = SenSettingsMenuDefaults.OuterPadding,
    contentPadding: PaddingValues = SenSettingsMenuDefaults.ContentPadding,
    colors: ListItemColors = SenSettingsMenuDefaults.neutralColors(),
    onClick: (() -> Unit)? = null,
) {
    val clickableModifier = if (onClick != null) modifier.clickable(onClick = onClick) else modifier

    /* I don't like wrapping things inside a "container" wrapper, especially a Box, but this is
    the only way to have an outer padding but still make the interaction full-width afaik */
    Box(modifier = clickableModifier.padding(outerPadding)) {
        SegmentedListItem(
            shapes = shapes,
            colors = colors,
            content = headlineContent,
            supportingContent = supportingContent,
            leadingContent = leadingContent,
            trailingContent = trailingContent,
            contentPadding = contentPadding,
            modifier = Modifier.heightIn(min = minHeight),
        )
    }
}

@Composable
fun SenSettingsMenuExtra(
    modifier: Modifier = Modifier,
    padding: PaddingValues = SenSettingsMenuExtraDefaults.Padding,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier = modifier.padding(padding)) {
        content()
    }
}
