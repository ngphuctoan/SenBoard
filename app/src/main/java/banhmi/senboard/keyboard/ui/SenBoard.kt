package banhmi.senboard.keyboard.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import banhmi.senboard.app.annotations.SenPreviewCommon
import banhmi.senboard.data.preferences.SenPreferences
import banhmi.senboard.keyboard.data.SenBoardState
import banhmi.senboard.keyboard.data.SenBoardStateDefaults
import banhmi.senboard.keyboard.data.ShiftMode
import banhmi.senboard.keyboard.impl.mode.SenCharactersMode
import banhmi.senboard.keyboard.model.SenKeyDisplay
import banhmi.senboard.keyboard.model.SenLayout
import banhmi.senboard.keyboard.model.SenLayoutKey
import banhmi.senboard.ui.theme.SenTheme
import banhmi.senboard.utils.IndexCount
import banhmi.senboard.utils.outOf
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

data class SenKeyPressContext(val selectedIndex: Int)

/* A custom PressInteraction that is meant to be sent alongside PressInteraction.Press,
and will provide SenKey the currently selected index for conditional indication */
class SenKeyPressInteraction(val context: SenKeyPressContext?) : PressInteraction

sealed interface SelectedIndexResult {
    object NotSelected : SelectedIndexResult

    class Selected(val index: Int) : SelectedIndexResult
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
                    delay(SenBoardDefaults.RepeatTimeoutMillis)
                }
            }

            val up = waitForUpOrCancellation()
            repeatingJob.cancel()

            if (up == null) {
                interactionSource?.tryEmit(cancelInteraction)
                return@awaitEachGesture
            }

            /* We know that the user tapped once/twice if the signal wasn't completed.
            Otherwise, we don't handle the rest of the actions below */
            if (repeatingJobStarted.isCompleted) {
                interactionSource?.tryEmit(releaseInteraction)
                return@awaitEachGesture
            }

            // This tells us the user tapped once before
            val uptimeMillisChanged = previousUptimeMillis != 0L
            // True when the user tap a second time before the timeout
            val doubleTapBeforeTimeout =
                up.uptimeMillis - previousUptimeMillis <= viewConfiguration.doubleTapTimeoutMillis
            // We treat tapping a different key before the timeout as a single tap
            val sameKeyTapped = previousContext == context

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

fun distanceToKey(keyOffset: Offset, keySize: Size, tapPosition: Offset): Float {
    val rect = Rect(keyOffset, keySize)

    val normalizedPosition =
        Offset(abs(tapPosition.x - rect.center.x), abs(tapPosition.y - rect.center.y))
    val oppositePosition = normalizedPosition - Offset(keySize.width / 2, keySize.height / 2)

    val outerDistance =
        Offset(max(oppositePosition.x, 0f), max(oppositePosition.y, 0f)).getDistance()
    val innerDistance = min(max(oppositePosition.x, oppositePosition.y), 0f)

    return outerDistance + innerDistance
}

data class SenBoardScaffoldColors(
    val color: Color,
    val contentColor: Color, // This only affects content that's not part of the keys
)

object SenBoardScaffoldDefaults {
    @Composable
    fun colors(): SenBoardScaffoldColors = SenBoardScaffoldColors(
        color = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurface,
    )

    val HorizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally

    @Composable
    internal fun elevation(): Float = with(LocalDensity.current) { 8.dp.toPx() }
}

@Composable
fun SenBoardScaffold(
    topBar: @Composable () -> Unit,
    // Mainly for navigation bars padding, but can be used to add extra functionalities in the future
    bottomBar: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = SenBoardScaffoldDefaults.HorizontalAlignment,
    colors: SenBoardScaffoldColors = SenBoardScaffoldDefaults.colors(),
    content: @Composable () -> Unit,
) {
    val elevation = SenBoardScaffoldDefaults.elevation()

    Column(
        horizontalAlignment = horizontalAlignment,
        modifier = modifier
            .graphicsLayer { shadowElevation = elevation }
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
    internal val RepeatTimeoutMillis: Duration = 50.milliseconds
}

@Composable
fun SenBoard(
    layout: SenLayout,
    onKeyTap: (Int) -> Unit,
    onKeyDoubleTap: (Int) -> Unit,
    onKeyLongTap: (Int) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.(IndexCount, Int, SenLayoutKey, InteractionSource) -> Unit,
) {
    var boardSize: Size by remember { mutableStateOf(Size.Zero) }
    var boardPosition: Offset by remember { mutableStateOf(Offset.Zero) }

    var selectedIndex: SelectedIndexResult by remember {
        mutableStateOf(SelectedIndexResult.NotSelected)
    }

    val interactionSource: MutableInteractionSource = remember {
        MutableInteractionSource()
    }

    val haptic = LocalHapticFeedback.current

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.senBoardCombinedClickable(
            onTap = {
                if (selectedIndex is SelectedIndexResult.Selected) {
                    onKeyTap((selectedIndex as SelectedIndexResult.Selected).index)
                }
            },
            onDoubleTap = {
                if (selectedIndex is SelectedIndexResult.Selected) {
                    onKeyDoubleTap((selectedIndex as SelectedIndexResult.Selected).index)
                }
            },
            onLongTap = {
                if (selectedIndex is SelectedIndexResult.Selected) {
                    onKeyLongTap((selectedIndex as SelectedIndexResult.Selected).index)
                }
            },
            onTapDown = { position ->
                haptic.performHapticFeedback(HapticFeedbackType.KeyboardTap)

                val keyIndexDistances: MutableList<Pair<Int, Float>> = mutableListOf()

                val keyBaseHeight = boardSize.height / layout.totalHeightMultipliers

                layout.rows.fold(initial = Pair(0, 0f)) { (currentIndex, y), row ->
                    val height = keyBaseHeight * row.heightMultiplier

                    val keyBaseWidth = boardSize.width / row.totalAreaWidthMultipliers

                    row.keys.foldIndexed(0f) { keyIndex, x, key ->
                        val width = keyBaseWidth * key.areaWidthMultiplier
                        val distance = distanceToKey(
                            Offset(x, y) + boardPosition,
                            Size(width, height),
                            position,
                        )
                        keyIndexDistances.add(Pair(currentIndex + keyIndex, distance))
                        x + width
                    }

                    Pair(currentIndex + row.keys.size, y + height)
                }

                val keyIndexDistance = keyIndexDistances.minBy { (_, distance) -> distance }
                val index = keyIndexDistance.first

                selectedIndex = SelectedIndexResult.Selected(index)
                SenKeyPressContext(index)
            },
            onTapUp = { selectedIndex = SelectedIndexResult.NotSelected },
            interactionSource = interactionSource,
        ),
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = SenKeyDefaults.ShapeMaxWidth * layout.maxTotalAreaWidthMultipliers)
                .height(SenKeyDefaults.AreaHeight * layout.totalHeightMultipliers)
                .onGloballyPositioned { coordinates ->
                    boardSize = coordinates.size.toSize()
                    boardPosition = coordinates.positionInParent()
                },
        ) {
            layout.rows.fold(initial = 0) { currentKeyDataIndex, row ->
                val numOfKeys = row.keys.size

                Row(
                    modifier = Modifier.weight(row.heightMultiplier),
                ) {
                    row.keys.forEachIndexed { keyIndex, key ->
                        val keyDataIndex = currentKeyDataIndex + keyIndex
                        val keyIndexCount = keyIndex outOf numOfKeys

                        content(keyIndexCount, keyDataIndex, key, interactionSource)
                    }
                }

                currentKeyDataIndex + numOfKeys
            }
        }
    }
}

@SenPreviewCommon
@Composable
fun SenBoardPreview() {
    var preferences by remember { mutableStateOf(SenPreferences(keyBackgroundEnabled = true)) }
    var state by remember {
        mutableStateOf(
            SenBoardState(
                mode = SenCharactersMode,
                shiftMode = ShiftMode.Off,
                composingText = SenBoardStateDefaults.EmptyComposingText,
                previousWord = SenBoardStateDefaults.EmptyPreviousWord,
            ),
        )
    }

    val mode = state.mode
    val layout = mode.layout

    SenTheme {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier.fillMaxSize(),
        ) {
            SenBoardScaffold(
                topBar = {
                    Row(modifier = Modifier.widthIn(max = SenKeyDefaults.ShapeMaxWidth * layout.maxTotalAreaWidthMultipliers)) {
                        SenEngineSwitcher(
                            engineType = preferences.vietnameseEngineType,
                            onEngineSwitch = { engineType ->
                                preferences = preferences.copy(
                                    vietnameseEngineType = engineType,
                                )
                            },
                        ) { engineType ->
                            SenEngineIcon(engineType)
                        }

                        SenSuggestions(
                            suggestions = listOf("this", "is", "cool"),
                            modifier = Modifier.weight(1f),
                        )

                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Outlined.AutoAwesome,
                                contentDescription = null,
                            )
                        }
                    }
                },
                bottomBar = {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding(),
                    )
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                SenBoard(
                    layout = layout,
                    onKeyTap = {},
                    onKeyDoubleTap = {},
                    onKeyLongTap = {},
                    modifier = Modifier.fillMaxWidth(),
                ) { _, index, key, interactionSource ->
                    val keyData = mode.keyDatas[index]

                    val style = keyData.styleProvider(state, preferences)

                    CompositionLocalProvider(LocalTextStyle provides style.textStyle) {
                        SenKey(
                            style = style,
                            areaWidthMultiplier = key.areaWidthMultiplier,
                            shapeWidthProportion = key.shapeWidthProportion,
                            shapeAlignment = key.shapeAlignment,
                            interactionSource = interactionSource,
                            indication = SenKeyIndication(
                                index = index,
                                color = SenKeyIndicationDefaults.color(),
                                shape = RectangleShape,
                            ),
                            modifier = Modifier.padding(3.dp, 6.dp),
                        ) {
                            when (val display = keyData.display(state)) {
                                is SenKeyDisplay.Text -> Text(
                                    text = display.text,
                                    autoSize = TextAutoSize.StepBased(maxFontSize = 24.sp),
                                    maxLines = 1,
                                )

                                is SenKeyDisplay.Icon -> Icon(
                                    imageVector = display.icon,
                                    contentDescription = display.description,
                                    modifier = Modifier
                                        .sizeIn(maxWidth = 24.dp, maxHeight = 24.dp)
                                        .fillMaxSize()
                                        .rotate(display.transforms.rotation),
                                )

                                else -> {}
                            }
                        }
                    }
                }
            }
        }
    }
}
