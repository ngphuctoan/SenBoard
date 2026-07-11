package banhmi.senboard.ime.keyboard.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import banhmi.senboard.ime.keyboard.core.SenBoardController
import banhmi.senboard.ime.keyboard.ui.scope.SenBoardScope
import banhmi.senboard.ime.keyboard.ui.scope.SenBoardScopeImpl

@Composable
fun SenBoardRoot(
    controller: SenBoardController,
    content: @Composable SenBoardScope.() -> Unit,
) {
    val scope = remember(controller) {
        SenBoardScopeImpl(controller = controller)
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp),
        color = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        scope.content()
    }
}
