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
import androidx.compose.material.icons.outlined.KeyboardHide
import androidx.compose.material.icons.outlined.Redeem
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import banhmi.senboard.ime.keyboard.core.SenBoardContext
import banhmi.senboard.ime.keyboard.core.SenBoardController
import banhmi.senboard.ime.keyboard.core.SenBoardState
import banhmi.senboard.ime.keyboard.data.modes.CharactersMode
import banhmi.senboard.ime.keyboard.models.ShiftMode
import banhmi.senboard.ime.keyboard.ui.scope.SenBoardScope
import banhmi.senboard.ime.keyboard.ui.scope.SenBoardScopeImpl
import banhmi.senboard.ime.keyboard.ui.toolbar.SenToolbar
import banhmi.senboard.ime.keyboard.ui.toolbar.SenToolbarButton
import banhmi.senboard.ime.keyboard.ui.toolbar.SenToolbarSwitchEngineIcon
import banhmi.senboard.ime.keyboard.ui.toolbar.SwitchEngineLabel
import banhmi.senboard.shared.settings.AppearanceSettings
import banhmi.senboard.shared.settings.InputMethodSettings
import banhmi.senboard.shared.settings.SenSettingsViewModel
import banhmi.senboard.shared.settings.SoundsAndHapticsSettings
import banhmi.senboard.ui.theme.SenBoardTheme
import kotlinx.coroutines.flow.MutableStateFlow
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow

@Composable
fun SenBoardFullscreenContainer(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        content()
    }
}

@Composable
fun SenBoardSurface(
    maxWidth: Dp = 900.dp,
    horizontalMargin: Dp = 8.dp,
    viewModel: SenSettingsViewModel = viewModel(factory = SenSettingsViewModel.Factory),
    onPositioned: (Int) -> Unit = {},
    content: @Composable () -> Unit,
) {
    val state by viewModel.appearanceState.collectAsStateWithLifecycle()

    val window = LocalWindowInfo.current

    val isTablet = window.containerDpSize.width > maxWidth + horizontalMargin * 2

    val radius = if (isTablet && !state.fullWidthKeyboard) 8.dp else 0.dp
    val horizontalPadding = if (isTablet && !state.fullWidthKeyboard) horizontalMargin else 0.dp

    val additionalWidth = if (isTablet) 0.dp else horizontalMargin * 2
    val widthModifier = if (state.fullWidthKeyboard) Modifier.fillMaxWidth()
    else Modifier.widthIn(max = maxWidth + additionalWidth)

    Surface(
        modifier = widthModifier
            .fillMaxHeight()
            .padding(horizontal = horizontalPadding)
            .onGloballyPositioned { coords ->
                onPositioned(coords.positionInWindow().y.toInt())
            }
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
