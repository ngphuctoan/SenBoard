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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import banhmi.senboard.ime.keyboard.models.KeyAltPopup
import banhmi.senboard.ime.keyboard.models.KeyHandler
import banhmi.senboard.ime.keyboard.models.KeyVariant

private fun Modifier.keyInteraction(
    source: MutableInteractionSource,
    onTap: () -> Unit,
    onDoubleTap: () -> Unit,
    hasPopup: Boolean,
    key: Any?,
): Modifier = pointerInput(key, source, onTap, onDoubleTap, hasPopup) {
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

                if (!hasPopup) {
                    while (isActive) {
                        onTap()
                        delay(50.milliseconds)
                    }
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

private fun Modifier.popupInteraction(
    popup: KeyAltPopup?,
    onShowPopup: (Offset) -> Unit,
    onPointerMove: (Offset) -> Unit,
    onPopupRelease: () -> Unit,
): Modifier = if (popup == null) this else pointerInput(popup) {
    val longPressTimeout = ViewConfiguration.getLongPressTimeout().toLong()
    coroutineScope {
        awaitEachGesture {
            val down = awaitFirstDown(requireUnconsumed = false)

            var isLongPressed = false
            var currentPosition = down.position
            val longPressJob = launch {
                delay(longPressTimeout.milliseconds)
                isLongPressed = true
                onShowPopup(currentPosition)
            }

            while (true) {
                val event = awaitPointerEvent()
                if (event.changes.any { it.pressed.not() }) {
                    break
                }
                val change = event.changes.firstOrNull()
                if (change != null) {
                    currentPosition = change.position
                    if (isLongPressed) {
                        onPointerMove(currentPosition)
                    } else {
                        val dx = currentPosition.x - down.position.x
                        val dy = currentPosition.y - down.position.y
                        val distance = kotlin.math.sqrt(dx * dx + dy * dy)
                        if (distance > viewConfiguration.touchSlop) {
                            longPressJob.cancel()
                        }
                    }
                }
            }
            longPressJob.cancel()
            if (isLongPressed) {
                onPopupRelease()
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
    context: SenBoardContext,
    popup: KeyAltPopup?,
    onPopupAction: (KeyHandler) -> Unit,
    desc: String?,
    onTap: () -> Unit,
    onDoubleTap: () -> Unit,
    content: @Composable SenBoardKeyScope.() -> Unit,
) {
    val source = remember { MutableInteractionSource() }
    val scope = remember(key, onTap, onDoubleTap) {
        SenBoardKeyScopeImpl(key, onTap, onDoubleTap, source)
    }

    var isPopupVisible by remember { mutableStateOf(false) }
    var isPopupDismissed by remember { mutableStateOf(false) }
    var pointerPosition by remember { mutableStateOf(Offset.Zero) }
    var keySize by remember { mutableStateOf(IntSize.Zero) }

    val density = LocalDensity.current

    // "Clean" code should look like this :b
    fun convertToPx(dp: Dp): Float = with(density) { dp.toPx() }
    fun convertToDp(px: Int): Dp = with(density) { px.toDp() }

    // This is what PEAK Kotlin looks like!!!
    fun getKeyWidthPx(): Float = popup?.keyWidth?.let { convertToPx(it) } ?: keySize.width.toFloat()
    fun getKeyHeightPx(): Float = popup?.keyHeight?.let { convertToPx(it) } ?: keySize.height.toFloat()

    val popupState by remember(popup, keySize, isPopupDismissed) {
        derivedStateOf {
            if (isPopupVisible && popup != null && keySize.width > 0 && keySize.height > 0 && !isPopupDismissed) {
                calculatePopupState(pointerPosition, popup, getKeyWidthPx(), getKeyHeightPx())
            } else {
                null
            }
        }
    }

    Box(
        modifier = Modifier
            .weight(key.areaWeight)
            .fillMaxHeight()
            .onGloballyPositioned { keySize = it.size }
            .semantics {
                role = Role.Button
                if (!desc.isNullOrEmpty()) contentDescription = desc
                onClick { onTap().let { true } }
            }
            .keyInteraction(source, onTap, onDoubleTap, hasPopup = popup != null, key = null)
            .popupInteraction(
                popup = popup,
                onShowPopup = { position ->
                    pointerPosition = position
                    isPopupVisible = true
                },
                onPointerMove = { position ->
                    pointerPosition = position
                    if (position.y > getKeyHeightPx()) isPopupDismissed = true
                },
                onPopupRelease = {
                    // Hovered item needs to be re-accessed in here to avoid "stale" null
                    val currentlyHoveredItem = popupState?.first
                    if (isPopupVisible && !isPopupDismissed && currentlyHoveredItem != null) {
                        onPopupAction(currentlyHoveredItem.handler)
                    }
                    isPopupVisible = false
                    isPopupDismissed = false
                    pointerPosition = Offset.Zero
                },
            ),
        contentAlignment = key.shapeAlignment,
    ) {
        scope.content()

        val hoveredItem = popupState?.first
        val popupOffset = popupState?.second

        if (isPopupVisible && !isPopupDismissed && popup != null && hoveredItem != null && popupOffset != null) {
            val keyWidth = popup.keyWidth ?: convertToDp(keySize.width)
            val keyHeight = popup.keyHeight ?: convertToDp(keySize.height)

            SenBoardKeyAltPopup(
                context = context,
                popupData = popup,
                hoveredItem = hoveredItem,
                keyWidth = keyWidth,
                keyHeight = keyHeight,
                style = KeyVariant.Ghost(context),
                activeStyle = KeyVariant.Primary(context),
                offset = popupOffset,
            )
        }
    }
}
