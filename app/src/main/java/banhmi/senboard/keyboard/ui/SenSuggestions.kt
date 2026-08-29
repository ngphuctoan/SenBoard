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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import banhmi.senboard.model.BigramResult

object SenSuggestionsDefaults {
    val Height = 48.dp

    internal val OriginalIconAlignment = Alignment.BottomCenter
}

@Composable
fun SenSuggestions(
    suggestions: List<BigramResult>,
    onSuggestionChoose: (String) -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = SenSuggestionsDefaults.Height,
    // Hard limit which is separate from take limit from bigram engine
    topK: Int = 3,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        val topSuggestions = suggestions.take(topK)

        topSuggestions.forEachIndexed { index, suggestion ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .height(height)
                    .clickable(onClick = { onSuggestionChoose(suggestion.text) }),
            ) {
                Text(text = suggestion.text)

                if (suggestion.isOriginal) {
                    Icon(
                        imageVector = Icons.Outlined.MoreHoriz,
                        contentDescription = "Từ ban đầu",
                        modifier = Modifier.align(SenSuggestionsDefaults.OriginalIconAlignment),
                    )
                }
            }

            if (index != topSuggestions.lastIndex) {
                VerticalDivider(modifier = Modifier.height(height / 2))
            }
        }
    }
}
