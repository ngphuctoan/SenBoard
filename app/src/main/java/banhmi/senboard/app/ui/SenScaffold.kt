package banhmi.senboard.app.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

object SenScaffoldDefaults {
    @Composable
    fun containerColor() = MaterialTheme.colorScheme.surfaceContainer
}

@Composable
fun SenScaffold(
    topBar: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = SenScaffoldDefaults.containerColor(),
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        topBar = topBar,
        containerColor = containerColor,
        modifier = modifier,
    ) { innerPadding ->
        content(innerPadding)
    }
}
