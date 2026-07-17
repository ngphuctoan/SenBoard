package banhmi.senboard.ime.keyboard.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import banhmi.senboard.ime.keyboard.core.SenBoardContext
import banhmi.senboard.ime.keyboard.core.SenBoardController
import banhmi.senboard.ime.keyboard.core.SenBoardState
import banhmi.senboard.ime.keyboard.data.modes.CharactersMode
import banhmi.senboard.ime.keyboard.models.ShiftMode
import banhmi.senboard.ime.keyboard.ui.scope.SenBoardScope
import banhmi.senboard.ime.keyboard.ui.scope.SenBoardScopeImpl
import banhmi.senboard.ime.keyboard.ui.toolbar.Toolbar
import banhmi.senboard.ui.theme.SenBoardTheme

@Composable
fun SenBoardRoot(
    content: @Composable () -> Unit,
) {
    val prefs = banhmi.senboard.app.settings.rememberPreferences()
    val keyboardHeight = prefs.keyboardHeight
    val navBarHeight = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(keyboardHeight.dp + navBarHeight)
            .navigationBarsPadding(),
        color = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        content()
    }
}

@Composable
fun SenBoardContent(
    controller: SenBoardController,
    content: @Composable SenBoardScope.() -> Unit,
) {
    BoxWithConstraints {
        val scope = remember(controller) {
            SenBoardScopeImpl(
                controller = controller,
                screenWidth = this.maxWidth,
            )
        }
        scope.content()
    }
}

@Preview(
    widthDp = 540,
    heightDp = 960,
)
@Composable
fun SenBoardPreview() {
    val initialState = SenBoardState(
        mode = CharactersMode,
        shiftMode = ShiftMode.Off,
    )

    val context = remember { SenBoardContext(im = null, initialState = initialState) }
    val controller = remember { SenBoardController(context) }

    SenBoardTheme(darkTheme = true) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter,
        ) {
            SenBoardRoot {
                SenBoardContent(controller) {
                    val state = controller.state
                    Column {
                        Toolbar {}
                        SenBoardLayout(
                            mode = state.mode,
                            onKeyTap = { controller.handle(it) },
                            onKeyDoubleTap = { controller.handleDoubleTap(it) },
                        )
                    }
                }
            }
        }
    }
}
