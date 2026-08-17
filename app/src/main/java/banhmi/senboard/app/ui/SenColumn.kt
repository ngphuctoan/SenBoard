package banhmi.senboard.app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

object SenColumnDefaults {
    val ContentPadding: PaddingValues = PaddingValues(16.dp, 8.dp)
}

@Composable
fun SenColumn(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
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
