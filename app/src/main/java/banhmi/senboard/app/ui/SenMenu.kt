package banhmi.senboard.app.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ListItemShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/* Index tells the settings the current position of the menu in the group,
and the count indicates the number of menus in the group */
data class IndexCount(
    val index: Int,
    val count: Int,
)

// I guess a more "written language" way of defining IndexCount :b
infix fun Int.outOf(count: Int): IndexCount = IndexCount(this, count)

object SenMenuDefaults {
    internal val MinHeight: Dp = 72.dp

    @Composable
    fun colors(): ListItemColors = ListItemDefaults.colors(
        containerColor = MaterialTheme.colorScheme.surfaceBright,
        disabledContainerColor = MaterialTheme.colorScheme.surfaceBright,
    )

    @Composable
    fun primaryColors(): ListItemColors = ListItemDefaults.colors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        disabledContainerColor = MaterialTheme.colorScheme.surfaceBright,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        supportingContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
    )

    @Composable
    fun segmentedShapes(indexCount: IndexCount): ListItemShapes =
        ListItemDefaults.segmentedShapes(indexCount.index, indexCount.count)

    @Composable
    fun circleShapes(): ListItemShapes = ListItemDefaults.shapes(
        shape = CircleShape,
        selectedShape = CircleShape,
        pressedShape = CircleShape,
        focusedShape = CircleShape,
        hoveredShape = CircleShape,
        draggedShape = CircleShape,
    )

    val ContentPadding = PaddingValues(16.dp)

    val CircleContentPadding: PaddingValues = ContentPadding.plus(PaddingValues(start = 16.dp))
}

@Composable
fun SenMenu(
    shapes: ListItemShapes,
    modifier: Modifier = Modifier,
    supportingContent: @Composable (() -> Unit)? = null,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    contentPadding: PaddingValues = SenMenuDefaults.ContentPadding,
    colors: ListItemColors = SenMenuDefaults.colors(),
    content: @Composable () -> Unit,
) {
    SegmentedListItem(
        shapes = shapes,
        supportingContent = supportingContent,
        leadingContent = leadingContent,
        trailingContent = trailingContent,
        contentPadding = contentPadding,
        colors = colors,
        modifier = modifier.heightIn(min = SenMenuDefaults.MinHeight),
    ) {
        content()
    }
}

@Composable
fun SenMenu(
    shapes: ListItemShapes,
    modifier: Modifier = Modifier,
    supportingContent: @Composable (() -> Unit)? = null,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    contentPadding: PaddingValues = ListItemDefaults.ContentPadding,
    colors: ListItemColors = SenMenuDefaults.colors(),
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    SegmentedListItem(
        shapes = shapes,
        supportingContent = supportingContent,
        leadingContent = leadingContent,
        trailingContent = trailingContent,
        contentPadding = contentPadding,
        colors = colors,
        onClick = onClick,
        modifier = modifier.heightIn(min = SenMenuDefaults.MinHeight),
    ) {
        content()
    }
}

@Composable
fun SenMenu(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    shapes: ListItemShapes,
    modifier: Modifier = Modifier,
    supportingContent: @Composable (() -> Unit)? = null,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    contentPadding: PaddingValues = ListItemDefaults.ContentPadding,
    colors: ListItemColors = SenMenuDefaults.colors(),
    content: @Composable () -> Unit,
) {
    SegmentedListItem(
        checked = checked,
        onCheckedChange = onCheckedChange,
        shapes = shapes,
        supportingContent = supportingContent,
        leadingContent = leadingContent,
        trailingContent = trailingContent,
        contentPadding = contentPadding,
        colors = colors,
        modifier = modifier.heightIn(min = SenMenuDefaults.MinHeight),
    ) {
        content()
    }
}

@Composable
fun SenMenu(
    selected: Boolean,
    shapes: ListItemShapes,
    modifier: Modifier = Modifier,
    supportingContent: @Composable (() -> Unit)? = null,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    contentPadding: PaddingValues = ListItemDefaults.ContentPadding,
    colors: ListItemColors = SenMenuDefaults.colors(),
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    SegmentedListItem(
        selected = selected,
        shapes = shapes,
        supportingContent = supportingContent,
        leadingContent = leadingContent,
        trailingContent = trailingContent,
        contentPadding = contentPadding,
        colors = colors,
        onClick = onClick,
        modifier = modifier.heightIn(min = SenMenuDefaults.MinHeight),
    ) {
        content()
    }
}
