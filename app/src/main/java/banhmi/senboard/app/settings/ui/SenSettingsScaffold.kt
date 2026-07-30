package banhmi.senboard.app.settings.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SenSettingsScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        topBar = topBar,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
    ) { innerPadding ->
        content(innerPadding)
    }
}
