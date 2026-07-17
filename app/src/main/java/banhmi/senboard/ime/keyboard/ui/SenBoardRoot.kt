package banhmi.senboard.ime.keyboard.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
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
fun SenBoardSurface(
    maxWidth: Dp = 900.dp,
    horizontalMargin: Dp = 8.dp,
    content: @Composable () -> Unit
) {
    val window = LocalWindowInfo.current

    val isTablet = window.containerDpSize.width > maxWidth + horizontalMargin * 2

    val radius = if (isTablet) 8.dp else 0.dp
    val horizontalPadding = if (isTablet) horizontalMargin else 0.dp
    val additionalWidth = if (isTablet) 0.dp else horizontalMargin * 2

    Surface(
        modifier = Modifier
            .widthIn(max = maxWidth + additionalWidth)
            .fillMaxHeight()
            .padding(horizontal = horizontalPadding)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {}, // Prevents onDismiss call from SenBoardRoot
            ),
        shape = RoundedCornerShape(topStart = radius, topEnd = radius),
        color = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        content()
    }
}

@Composable
fun SenBoardRoot(
    preferredHeight: Dp = 320.dp,
    minHeightRatio: Float = 0.65f,
    onDismiss: () -> Unit = {},
    content: @Composable BoxWithConstraintsScope.() -> Unit,
) {
    val density = LocalDensity.current
    val window = LocalWindowInfo.current

    // Keyboard should be at least minHeightRatio% of screen height, otherwise prefer preferredHeight
    val keyboardHeight = minOf(
        window.containerDpSize.height * minHeightRatio.coerceIn(0f, 1f),
        preferredHeight,
    )
    val navBarHeight = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    CompositionLocalProvider(LocalDensity provides Density(density.density, fontScale = 1f)) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(keyboardHeight + navBarHeight)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismiss,
                ),
            contentAlignment = Alignment.BottomCenter,
        ) {
            content()
        }
    }
}

@Composable
fun BoxWithConstraintsScope.SenBoardContent(
    controller: SenBoardController,
    content: @Composable SenBoardScope.() -> Unit,
) {
    val scope = remember(controller) {
        SenBoardScopeImpl(
            controller = controller,
            screenWidth = this.maxWidth,
            screenHeight = this.maxHeight,
        )
    }
    Box(
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxSize()
    ) {
        scope.content()
    }
}

@Preview(widthDp = 280, heightDp = 600)
@Composable
fun SenBoardNarrowPreview() {
    SenBoardPreview()
}

@PreviewScreenSizes
@Composable
fun SenBoardPreview() {
    val initialState = SenBoardState(
        mode = CharactersMode,
        shiftMode = ShiftMode.Off,
    )

    val context = remember { SenBoardContext(im = null, initialState = initialState) }
    val controller = remember { SenBoardController(context) }

    SenBoardTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter,
        ) {
            SenBoardRoot {
                SenBoardSurface {
                    SenBoardContent(controller) {
                        val state = controller.state
                        Column {
                            Toolbar {
                                IconButton(onClick = {}) {
                                    Icon(Icons.Outlined.Edit, contentDescription = null)
                                }
                                IconButton(onClick = {}) {
                                    Icon(Icons.Outlined.DarkMode, contentDescription = null)
                                }
                                IconButton(onClick = {}) {
                                    Icon(Icons.Outlined.Settings, contentDescription = null)
                                }
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
    }
}
