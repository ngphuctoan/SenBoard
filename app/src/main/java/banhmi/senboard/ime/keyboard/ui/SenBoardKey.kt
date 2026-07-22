package banhmi.senboard.ime.keyboard.ui

import android.view.ViewConfiguration
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import banhmi.senboard.ime.keyboard.core.SenBoardContext
import banhmi.senboard.ime.keyboard.core.SenBoardController
import banhmi.senboard.ime.keyboard.models.Key
import banhmi.senboard.ime.keyboard.models.invoke
import banhmi.senboard.ime.keyboard.ui.indications.KeyHighlightIndication
import banhmi.senboard.ime.keyboard.ui.scope.SenBoardKeyScope
import banhmi.senboard.ime.keyboard.ui.scope.SenBoardKeyScopeImpl
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

private fun Modifier.keyInteraction(
    source: MutableInteractionSource,
    onTap: () -> Unit,
    onDoubleTap: () -> Unit,
    key: Any?,
): Modifier = pointerInput(key, source, onTap, onDoubleTap) {
    val doubleTapTimeout = ViewConfiguration.getDoubleTapTimeout().toLong()
    val longPressTimeout = ViewConfiguration.getLongPressTimeout().toLong()

    var lastTapUpTime = 0L

    coroutineScope {
        awaitEachGesture {
            val down = awaitFirstDown().also { it.consume() }

            val tapInteraction = PressInteraction.Press(down.position)
            source.tryEmit(tapInteraction)

            var hasRepeated = false

            val repeatJob = launch {
                delay(longPressTimeout.milliseconds)

                hasRepeated = true
                lastTapUpTime = 0L

                while (isActive) {
                    onTap()
                    delay(50.milliseconds)
                }
            }

            val upChanged = try {
                waitForUpOrCancellation()
            } finally {
                repeatJob.cancel()
            }

            if (upChanged == null) {
                val tapCancelInteraction = PressInteraction.Cancel(tapInteraction)
                source.tryEmit(tapCancelInteraction)
                return@awaitEachGesture
            }

            val tapReleaseInteraction = PressInteraction.Release(tapInteraction)
            source.tryEmit(tapReleaseInteraction)

            if (hasRepeated) return@awaitEachGesture

            val timeout = upChanged.uptimeMillis - lastTapUpTime
            val isDoubleTap = lastTapUpTime != 0L && timeout <= doubleTapTimeout

            if (isDoubleTap) {
                onDoubleTap()
                lastTapUpTime = 0L
            } else {
                onTap()
                lastTapUpTime = upChanged.uptimeMillis
            }
        }
    }
}

@Composable
fun SenBoardKeyScope.SenBoardKeyContent(
    contentPadding: Dp = 8.dp,
    content: @Composable SenBoardKeyScope.() -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@Composable
fun SenBoardKeyScope.SenBoardKeyShape(
    context: SenBoardContext,
    margin: PaddingValues = PaddingValues(8.dp),
    forceHighlightState: Boolean? = null,
    content: @Composable SenBoardKeyScope.() -> Unit,
) {
    val style = key.variant(context)
    val pressedColor = if (isSystemInDarkTheme()) Color.White else Color.Black

    Surface(
        modifier = Modifier
            .fillMaxWidth(key.shapeWeight / key.areaWeight)
            .fillMaxHeight()
            .padding(margin)
            .indication(
                interactionSource = source,
                indication = KeyHighlightIndication(
                    color = pressedColor.copy(alpha = 0.2f),
                    shape = style.shape,
                    forceState = forceHighlightState,
                ),
            ),
        color = style.color,
        contentColor = style.contentColor,
        shape = style.shape,
    ) {
        this.content()
    }
}

@Composable
fun RowScope.SenBoardKeyArea(
    key: Key,
    desc: String?,
    onTap: () -> Unit,
    onDoubleTap: () -> Unit,
    content: @Composable SenBoardKeyScope.() -> Unit,
) {
    val source = remember { MutableInteractionSource() }
    val scope = remember(key, onTap, onDoubleTap) {
        SenBoardKeyScopeImpl(key, onTap, onDoubleTap, source)
    }

    Box(
        modifier = Modifier
            .weight(key.areaWeight)
            .fillMaxHeight()
            .semantics {
                role = Role.Button
                if (!desc.isNullOrEmpty()) contentDescription = desc
                onClick { onTap().let { true } }
            }
            .keyInteraction(source, onTap, onDoubleTap, key = null),
        contentAlignment = key.shapeAlignment,
    ) {
        scope.content()
    }
}
