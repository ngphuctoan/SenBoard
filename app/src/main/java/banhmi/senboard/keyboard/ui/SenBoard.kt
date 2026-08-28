package banhmi.senboard.keyboard.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import banhmi.senboard.app.annotations.SenPreviewCommon
import banhmi.senboard.data.preferences.SenPreferences
import banhmi.senboard.keyboard.impl.handler.SenShiftKeyHandler
import banhmi.senboard.keyboard.model.SenLayout
import banhmi.senboard.keyboard.model.SenLayoutKey
import banhmi.senboard.keyboard.state.SenBoardState
import banhmi.senboard.keyboard.state.ShiftMode
import banhmi.senboard.ui.theme.SenTheme
import banhmi.senboard.utils.RectangleSDF
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

data class SenKeyPressContext(
    val selectedIndex: Int,
)

/* A custom PressInteraction that is meant to be sent alongside PressInteraction.Press,
and will provide SenKey the currently selected index for conditional indication */
class SenKeyPressInteraction(
    val context: SenKeyPressContext?,
) : PressInteraction

sealed interface SelectedIndexResult {
    object NotSelected : SelectedIndexResult

    class Selected(
        val index: Int,
    ) : SelectedIndexResult
}

object SenBoardCombinedClickableDefaults {
    internal val RepeatTimeoutMillis: Duration = 50.milliseconds
}

/* A custom modifier for handling tap interactions. This is different from combinedClickable,
because this calls the onTap handler without delay (i.e. call before the double tap timeout) */
@Composable
fun Modifier.senBoardCombinedClickable(
    // These are for actual interactions
    onTap: () -> Unit,
    onDoubleTap: () -> Unit,
    onLongTap: () -> Unit,
    // These are for optional logics that need these events
    onTapDown: (Offset) -> SenKeyPressContext? = { null },
    onTapUp: (Offset) -> Unit = {},
    onTapCancel: () -> Unit = {},
    interactionSource: MutableInteractionSource? = null,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): Modifier {
    var previousUptimeMillis: Long by remember { mutableLongStateOf(0L) }
    var previousContext: SenKeyPressContext? by remember { mutableStateOf(null) }

    // Handlers must be included as keys for them to be recomposed, otherwise states inside won't get updated
    return pointerInput(
        onTap,
        onDoubleTap,
        onLongTap,
        onTapDown,
        onTapUp,
        interactionSource,
        coroutineScope,
    ) {
        awaitEachGesture {
            // Wait for the pointer down event
            val down = awaitFirstDown(requireUnconsumed = false)

            val tapInteraction = PressInteraction.Press(down.position)
            val releaseInteraction = PressInteraction.Release(tapInteraction)
            val cancelInteraction = PressInteraction.Cancel(tapInteraction)

            val context = onTapDown(down.position)
            // Emit the PressInteraction.Press event for semantics
            interactionSource?.tryEmit(tapInteraction)

            // SenKeyPressInteraction is going to be used for highlighting the selected key
            val keyPressInteraction = SenKeyPressInteraction(context)
            interactionSource?.tryEmit(keyPressInteraction)

            // This is a signal to know if the user was long tapping
            val repeatingJobStarted: CompletableDeferred<Unit> = CompletableDeferred()

            val repeatingJob = coroutineScope.launch {
                delay(viewConfiguration.longPressTimeoutMillis.milliseconds)

                repeatingJobStarted.complete(Unit)
                previousUptimeMillis = 0L // We don't count this as a single tap

                while (true) {
                    onLongTap()
                    delay(SenBoardCombinedClickableDefaults.RepeatTimeoutMillis)
                }
            }

            val up = waitForUpOrCancellation()
            repeatingJob.cancel()

            if (up == null) {
                onTapCancel()
                interactionSource?.tryEmit(cancelInteraction)
                return@awaitEachGesture
            }

            /* We know that the user tapped once/twice if the signal wasn't completed.
            Otherwise, we don't handle the rest of the actions below */
            if (repeatingJobStarted.isCompleted) {
                // Don't forget that releasing from tapping down is also counted as a tap up
                onTapUp(up.position)
                interactionSource?.tryEmit(releaseInteraction)
                return@awaitEachGesture
            }

            // This tells us the user tapped once before
            val uptimeMillisChanged = previousUptimeMillis != 0L
            // True when the user tap a second time before the timeout
            val doubleTapBeforeTimeout = up.uptimeMillis - previousUptimeMillis <= viewConfiguration.doubleTapTimeoutMillis
            // We treat tapping a different key before the timeout as a single tap
            val sameKeyTapped = previousContext?.selectedIndex == context?.selectedIndex

            if (uptimeMillisChanged && doubleTapBeforeTimeout && sameKeyTapped) {
                onDoubleTap()
                previousUptimeMillis = 0L
            } else {
                onTap()
                previousUptimeMillis = up.uptimeMillis
            }

            onTapUp(up.position) // Only triggered when released, because up won't be null
            interactionSource?.tryEmit(releaseInteraction)

            previousContext = context
        }
    }
}

data class SenBoardScaffoldColors(
    val color: Color,
    val contentColor: Color, // This only affects content that's not part of the keys
)

object SenBoardScaffoldDefaults {
    @Composable
    fun colors() = SenBoardScaffoldColors(
        color = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurface,
    )

    val HorizontalAlignment = Alignment.CenterHorizontally

    @Composable
    fun defaultShadowElevation() = with(LocalDensity.current) {
        0.dp.toPx()
    }

    @Composable
    fun shadowElevation(
        keyBackgroundShadowEnabled: Boolean,
    ) = with(LocalDensity.current) {
        if (keyBackgroundShadowEnabled) 8.dp.toPx() else defaultShadowElevation()
    }
}

@Composable
fun SenBoardScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    // Mainly for navigation bars padding, but can be used to add extra functionalities in the future
    bottomBar: @Composable () -> Unit = {},
    horizontalAlignment: Alignment.Horizontal = SenBoardScaffoldDefaults.HorizontalAlignment,
    colors: SenBoardScaffoldColors = SenBoardScaffoldDefaults.colors(),
    shadowElevation: Float = SenBoardScaffoldDefaults.defaultShadowElevation(),
    content: @Composable () -> Unit,
) {
    Column(
        horizontalAlignment = horizontalAlignment,
        modifier = modifier
            .graphicsLayer {
                this.shadowElevation = shadowElevation
            }
            .background(color = colors.color),
    ) {
        CompositionLocalProvider(LocalContentColor provides colors.contentColor) {
            Box { topBar() }
            Box { content() }
            Box { bottomBar() }
        }
    }
}

object SenBoardDefaults {
    internal val ContentAlignment = Alignment.Center
}

@Composable
fun SenBoard(
    layout: SenLayout,
    popup: @Composable (Offset, Offset) -> Unit,
    onKeyTap: (Int) -> Unit,
    onKeyDoubleTap: (Int) -> Unit,
    onKeyLongTap: (Int) -> Unit,
    modifier: Modifier = Modifier,
    onKeyTapDown: (Int) -> Unit = {},
    onKeyTapUp: (Int) -> Unit = {},
    onKeyTapCancel: () -> Unit = {},
    content: @Composable (Int, SenLayoutKey, InteractionSource) -> Unit,
) {
    val haptic = LocalHapticFeedback.current

    val interactionSource = remember { MutableInteractionSource() }
    var tapPosition by remember { mutableStateOf(Offset.Zero) }
    val keyRectangles: MutableMap<Int, Rect> = remember { mutableMapOf() }
    var selectedIndex: SelectedIndexResult by remember {
        mutableStateOf(SelectedIndexResult.NotSelected)
    }

    @Suppress("LocalVariableName")
    Box(
        contentAlignment = SenBoardDefaults.ContentAlignment,
        modifier = modifier.senBoardCombinedClickable(
            onTap = {
                if (selectedIndex is SelectedIndexResult.Selected) {
                    val _selectedIndex = selectedIndex as SelectedIndexResult.Selected
                    onKeyTap(_selectedIndex.index)
                }
            },
            onDoubleTap = {
                if (selectedIndex is SelectedIndexResult.Selected) {
                    val _selectedIndex = selectedIndex as SelectedIndexResult.Selected
                    onKeyDoubleTap(_selectedIndex.index)
                }
            },
            onLongTap = {
                if (selectedIndex is SelectedIndexResult.Selected) {
                    val _selectedIndex = selectedIndex as SelectedIndexResult.Selected
                    onKeyLongTap(_selectedIndex.index)
                }
            },
            onTapDown = { position ->
                haptic.performHapticFeedback(HapticFeedbackType.KeyboardTap)

                tapPosition = position

                val bestKey = keyRectangles.entries.minBy { (_, rectangle) ->
                    RectangleSDF(rectangle).distanceToPoint(position)
                }

                selectedIndex = SelectedIndexResult.Selected(bestKey.key)
                onKeyTapDown(bestKey.key)
                SenKeyPressContext(bestKey.key)
            },
            onTapUp = { position ->
                tapPosition = position

                if (selectedIndex is SelectedIndexResult.Selected) {
                    val _selectedIndex = selectedIndex as SelectedIndexResult.Selected
                    onKeyTapUp(_selectedIndex.index)
                    selectedIndex = SelectedIndexResult.NotSelected
                }
            },
            onTapCancel = {
                onKeyTapCancel()
                selectedIndex = SelectedIndexResult.NotSelected
            },
            interactionSource = interactionSource,
        ),
    ) {
        Column(modifier = Modifier.widthIn(max = 640.dp)) {
            layout.rows.fold(initial = 0) { currentKeyDataIndex, row ->
                val numOfKeys = row.keys.size

                Row(modifier = Modifier.weight(row.heightMultiplier)) {
                    row.keys.forEachIndexed { keyIndex, key ->
                        val keyDataIndex = currentKeyDataIndex + keyIndex

                        // This box is for measuring the position and size of the key
                        Box(
                            contentAlignment = key.shapeAlignment + Alignment.CenterVertically,
                            modifier = Modifier
                                .weight(key.areaWidthMultiplier)
                                .fillMaxHeight()
                                .onGloballyPositioned { coordinates ->
                                    val rowCoordinates = coordinates.parentLayoutCoordinates!!
                                    val columnCoordinates = rowCoordinates.parentLayoutCoordinates!!
                                    keyRectangles[keyDataIndex] = Rect(
                                        offset = coordinates.positionInParent() //
                                                + rowCoordinates.positionInParent() //
                                                + columnCoordinates.positionInParent(),
                                        size = coordinates.size.toSize(),
                                    )
                                },
                        ) {
                            content(keyDataIndex, key, interactionSource)
                        }
                    }
                }

                currentKeyDataIndex + numOfKeys
            }
        }

        if (selectedIndex is SelectedIndexResult.Selected) {
            val _selectedIndex = selectedIndex as SelectedIndexResult.Selected
            val topLeftOffset = keyRectangles[_selectedIndex.index]!!.topLeft
            popup(topLeftOffset, tapPosition)
        }
    }
}

@SenPreviewCommon
@Composable
fun SenBoardPreview() {
    var state by remember { mutableStateOf(SenBoardState()) }
    var preferences by remember {
        mutableStateOf(
            SenPreferences(
                keyBackgroundEnabled = true,
                keyBackgroundShadowEnabled = true,
            ),
        )
    }

    SenTheme {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier.fillMaxSize(),
        ) {
            SenBoardScaffold(
                bottomBar = {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding(),
                    )
                },
                shadowElevation = SenBoardScaffoldDefaults.shadowElevation(
                    preferences.keyBackgroundShadowEnabled,
                ),
                modifier = Modifier.fillMaxWidth(),
            ) {
                SenBoard(
                    layout = state.mode.layout,
                    popup = { _, _ -> },
                    onKeyTap = {},
                    onKeyDoubleTap = {},
                    onKeyLongTap = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                ) { index, key, interactionSource ->
                    val keyData = state.mode.keyDatas[index]

                    val shouldOverrideState = keyData.handler is SenShiftKeyHandler //
                            && state.shiftMode == ShiftMode.CapsLocked

                    SenKey(
                        key = key,
                        style = keyData.styleProvider(
                            state,
                            preferences,
                            isSystemInDarkTheme(),
                        ),
                        indication = SenKeyIndication(
                            index = index,
                            shape = RectangleShape,
                            color = SenKeyIndicationDefaults.color(),
                            overrideState = if (shouldOverrideState) true else null,
                        ),
                        interactionSource = interactionSource,
                    ) {
                        SenDisplay(
                            display = keyData.display(state),
                        )
                    }
                }
            }
        }
    }
}
