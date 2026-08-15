package banhmi.senboard.app.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ListItemShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

fun IndexCount.isLast(): Boolean = index == count - 1

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
        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
        disabledContainerColor = MaterialTheme.colorScheme.surfaceBright,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        selectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        supportingContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        selectedSupportingContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
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

    val ContentPadding: PaddingValues = PaddingValues(16.dp)

    val CircleContentPadding: PaddingValues = ContentPadding + PaddingValues(start = 16.dp)

    val SupportingControlsPadding: PaddingValues = PaddingValues(horizontal = 8.dp)
}

object SenMenuHasActionTrailingContentDefaults {
    internal val VerticalAlignment: Alignment.Vertical = Alignment.CenterVertically

    internal val DividerHeight: Dp = 40.dp

    internal val DividerPadding: PaddingValues = PaddingValues(start = 6.dp, end = 12.dp)

    @Composable
    internal fun dividerColor(): Color = MaterialTheme.colorScheme.outline
}

val LocalLastMenuPadding: ProvidableCompositionLocal<PaddingValues> =
    compositionLocalOf { PaddingValues(bottom = 16.dp) }

@Composable
fun Modifier.lastMenuPadding(): Modifier = padding(LocalLastMenuPadding.current)

@Composable
fun SenMenu(
    shapes: ListItemShapes,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    supportingContent: @Composable (() -> Unit)? = null,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    contentPadding: PaddingValues = SenMenuDefaults.ContentPadding,
    colors: ListItemColors = SenMenuDefaults.colors(),
    content: @Composable () -> Unit,
) {
    SegmentedListItem(
        enabled = enabled,
        shapes = shapes,
        supportingContent = supportingContent,
        leadingContent = leadingContent,
        trailingContent = trailingContent,
        verticalAlignment = verticalAlignment,
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
    enabled: Boolean = true,
    supportingContent: @Composable (() -> Unit)? = null,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    contentPadding: PaddingValues = ListItemDefaults.ContentPadding,
    colors: ListItemColors = SenMenuDefaults.colors(),
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    SegmentedListItem(
        enabled = enabled,
        shapes = shapes,
        supportingContent = supportingContent,
        leadingContent = leadingContent,
        trailingContent = trailingContent,
        verticalAlignment = verticalAlignment,
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
    enabled: Boolean = true,
    supportingContent: @Composable (() -> Unit)? = null,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    contentPadding: PaddingValues = ListItemDefaults.ContentPadding,
    colors: ListItemColors = SenMenuDefaults.colors(),
    content: @Composable () -> Unit,
) {
    SegmentedListItem(
        enabled = enabled,
        checked = checked,
        onCheckedChange = onCheckedChange,
        shapes = shapes,
        supportingContent = supportingContent,
        leadingContent = leadingContent,
        trailingContent = trailingContent,
        verticalAlignment = verticalAlignment,
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
    enabled: Boolean = true,
    supportingContent: @Composable (() -> Unit)? = null,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    contentPadding: PaddingValues = ListItemDefaults.ContentPadding,
    colors: ListItemColors = SenMenuDefaults.colors(),
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    SegmentedListItem(
        enabled = enabled,
        selected = selected,
        shapes = shapes,
        supportingContent = supportingContent,
        leadingContent = leadingContent,
        trailingContent = trailingContent,
        verticalAlignment = verticalAlignment,
        contentPadding = contentPadding,
        colors = colors,
        onClick = onClick,
        modifier = modifier.heightIn(min = SenMenuDefaults.MinHeight),
    ) {
        content()
    }
}

@Composable
fun SenMenuHasActionTrailingContent(
    modifier: Modifier = Modifier,
    actionDescription: String? = null,
    content: @Composable () -> Unit,
) {
    Row(
        verticalAlignment = SenMenuHasActionTrailingContentDefaults.VerticalAlignment,
        modifier = modifier,
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = actionDescription,
        )
        VerticalDivider(
            color = SenMenuHasActionTrailingContentDefaults.dividerColor(),
            modifier = Modifier
                .height(SenMenuHasActionTrailingContentDefaults.DividerHeight)
                .padding(SenMenuHasActionTrailingContentDefaults.DividerPadding),
        )
        content()
    }
}
