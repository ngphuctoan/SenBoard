package banhmi.senboard.app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.ListItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object SenColumnDefaults {
    val ContentPadding: PaddingValues = PaddingValues(16.dp, 8.dp)

    val SpacerSpacing: Dp = 16.dp
}

@Composable
fun SenColumnSpacer(
    modifier: Modifier = Modifier,
    spacing: Dp = SenColumnDefaults.SpacerSpacing,
) {
    Spacer(
        modifier = modifier
            .fillMaxWidth()
            .height(spacing),
    )
}

@Composable
fun SenColumn(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(ListItemDefaults.SegmentedGap),
    contentPadding: PaddingValues = SenColumnDefaults.ContentPadding,
    content: LazyListScope.() -> Unit,
) {
    LazyColumn(
        verticalArrangement = verticalArrangement,
        contentPadding = contentPadding,
        modifier = modifier,
    ) {
        content()
    }
}
