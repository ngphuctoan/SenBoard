package banhmi.senboard.keyboard.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SenSuggestions(
    suggestions: List<String>,
    modifier: Modifier = Modifier,
) {
    val topSuggestions = suggestions.take(3)
    val defaultHighlightedSuggestion: Int = topSuggestions.size / 2

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        topSuggestions.forEachIndexed { index, suggestion ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .clickable(onClick = {}),
            ) {
                Text(text = suggestion)

                if (index == defaultHighlightedSuggestion) {
                    Icon(
                        imageVector = Icons.Outlined.MoreHoriz,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.BottomCenter),
                    )
                }
            }

            if (index != topSuggestions.lastIndex) {
                VerticalDivider(modifier = Modifier.height(24.dp))
            }
        }
    }
}
