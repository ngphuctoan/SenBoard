package banhmi.senboard.ime.keyboard.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import banhmi.senboard.ime.keyboard.core.SenBoardContext
import banhmi.senboard.ime.keyboard.core.SenBoardController
import banhmi.senboard.ime.keyboard.core.SenBoardState
import banhmi.senboard.ime.keyboard.data.modes.AaaaaMode
import banhmi.senboard.ime.keyboard.data.modes.CharactersMode
import banhmi.senboard.ime.keyboard.models.ShiftMode
import banhmi.senboard.ime.keyboard.ui.scope.SenBoardScope
import banhmi.senboard.ime.keyboard.ui.scope.SenBoardScopeImpl
import banhmi.senboard.ime.keyboard.ui.toolbar.Toolbar
import banhmi.senboard.ui.theme.SenBoardTheme

@Composable
fun SenBoardRoot(
    controller: SenBoardController,
    widthSizeClass: WindowWidthSizeClass,
    content: @Composable SenBoardScope.() -> Unit,
) {
    val scope = remember(controller) {
        SenBoardScopeImpl(
            controller = controller,
            widthSizeClass = widthSizeClass,
        )
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(360.dp),
        color = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        scope.content()
    }
}

@SenBoardDevicePreviews
@Composable
fun SenBoardPreview() {
    val initialState = SenBoardState(
        mode = AaaaaMode,
        shiftMode = ShiftMode.Off,
    )

    val context = remember {
        SenBoardContext(
            im = null,
            initialState = initialState,
        )
    }
    val controller = remember {
        SenBoardController(context)
    }

    SenBoardTheme(darkTheme = true) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter,
        ) {
            SenBoardRoot(
                controller = controller,
                widthSizeClass = WindowWidthSizeClass.Medium,
            ) {
                val state = controller.state

                Column {
                    Toolbar {
//                        Text("Work in progress!!!", fontSize = 20.sp)
                    }
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

@Preview(
    name = "Mobile",
    widthDp = 540,
    heightDp = 960,
)
annotation class SenBoardDevicePreviews
