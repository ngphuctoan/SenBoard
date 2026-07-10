package banhmi.senboard.keyboard

import android.view.ViewConfiguration
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import banhmi.senboard.keyboard.keys.KeyAction
import banhmi.senboard.keyboard.keys.KeyData
import banhmi.senboard.keyboard.keys.KeyDisplay
import banhmi.senboard.keyboard.keys.KeyHighlightIndication
import banhmi.senboard.keyboard.keys.KeyVariant
import banhmi.senboard.keyboard.keys.provideKeyStyle
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

interface LayoutScope {
    val slots: List<KeyData>
    val manager: SenBoardManager
}

class LayoutColumnScopeImpl(
    override val slots: List<KeyData>,
    override val manager: SenBoardManager,
    private val scope: ColumnScope,
) : LayoutScope, ColumnScope by scope {
    private var i = 0

    fun nextSlotIndex() = i++
}

class LayoutRowScopeImpl(
    private val row: LayoutColumnScopeImpl,
    override val manager: SenBoardManager,
    private val scope: RowScope,
) : LayoutScope, RowScope by scope {
    override val slots get() = row.slots

    fun nextSlotIndex() = row.nextSlotIndex()
}

@Composable
fun LayoutRowScopeImpl.Key(
    weight: Float = 1f,
    keyWeight: Float = weight,
    keyAlignment: Alignment = Alignment.CenterStart,
    variant: KeyVariant = KeyVariant.Neutral,
) {
    val slotIndex = remember { nextSlotIndex() }
    val slot = slots[slotIndex]

    val interactionSource = remember { MutableInteractionSource() }

    val style = provideKeyStyle(variant)
    val pressedColor = if (isSystemInDarkTheme()) Color.White else Color.Black

    Box(
        modifier = Modifier
            .weight(weight)
            .fillMaxHeight()
            .semantics {
                role = Role.Button
                onClick {
                    manager.handle(slot.action)
                    true
                }
            }
            .pointerInput(slot.action, interactionSource) {
                var lastTapUpTime = 0L
                val doubleTapTimeout = ViewConfiguration.getDoubleTapTimeout().toLong()

                coroutineScope repeat@{
                    awaitEachGesture {
                        val down = awaitFirstDown()
                        down.consume()

                        val press = PressInteraction.Press(down.position)
                        interactionSource.tryEmit(press)

                        var hasRepeated = false

                        val repeatJob = this@repeat.launch {
                            delay(
                                ViewConfiguration.getLongPressTimeout().toLong().milliseconds
                            )

                            hasRepeated = true
                            lastTapUpTime = 0L

                            while (isActive) {
                                manager.handle(slot.action)
                                delay(50.milliseconds)
                            }
                        }

                        val up = try {
                            waitForUpOrCancellation()
                        } finally {
                            repeatJob.cancel()
                        }

                        if (up != null) {
                            interactionSource.tryEmit(
                                PressInteraction.Release(press)
                            )

                            if (!hasRepeated) {
                                val now = up.uptimeMillis

                                val isDoubleTap =
                                    lastTapUpTime != 0L && now - lastTapUpTime <= doubleTapTimeout

                                if (isDoubleTap) {
                                    manager.handleDoubleTap(slot.action)
                                    lastTapUpTime = 0L
                                } else {
                                    manager.handle(slot.action)
                                    lastTapUpTime = now
                                }
                            }
                        } else {
                            interactionSource.tryEmit(
                                PressInteraction.Cancel(press)
                            )

                            lastTapUpTime = 0L
                        }
                    }
                }
            },
        contentAlignment = keyAlignment,
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(keyWeight / weight)
                .fillMaxHeight()
                .padding(8.dp)
                .indication(
                    interactionSource = interactionSource,
                    indication = KeyHighlightIndication(
                        color = pressedColor.copy(alpha = 0.2f),
                        shape = style.shape,
                        forceVisible = slot.action == KeyAction.Shift && this@Key.manager.context.state.shiftMode == ShiftMode.CapsLocked,
                    ),
                ),
            color = style.color,
            contentColor = style.contentColor,
            shape = style.shape,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .alpha(0.75f)
                ) {
                    when (slot.supportDisplay) {
                        is KeyDisplay.Text -> Text(
                            text = slot.supportDisplay.label,
                            style = style.typography.supportLabel,
                        )

                        is KeyDisplay.Icon -> Icon(
                            slot.supportDisplay.icon,
                            contentDescription = null,
                            modifier = Modifier.size(style.iconSizes.support),
                        )

                        is KeyDisplay.Shift -> {}

                        is KeyDisplay.None -> {}
                    }
                }
                when (slot.display) {
                    is KeyDisplay.Text -> Text(
                        text = if (this@Key.manager.context.state.isShifted) slot.display.label.uppercase()
                        else slot.display.label.lowercase(),
                        style = style.typography.label,
                    )

                    is KeyDisplay.Icon -> Icon(
                        slot.display.icon,
                        contentDescription = null,
                        modifier = Modifier.size(style.iconSizes.main),
                    )

                    is KeyDisplay.Shift -> {
                        val iconDisplay =
                            slot.display.getIcon(this@Key.manager.context.state.shiftMode)!!
                        val description =
                            slot.display.getDescription(this@Key.manager.context.state.shiftMode)!!
                        Icon(
                            iconDisplay.icon,
                            contentDescription = description,
                            modifier = Modifier
                                .size(style.iconSizes.main)
                                .rotate(iconDisplay.rotation),
                        )
                    }

                    is KeyDisplay.None -> {}
                }
            }
        }
    }
}

@Composable
fun LayoutColumnScopeImpl.KeyRow(content: @Composable LayoutRowScopeImpl.() -> Unit) {
    Row(
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
    ) {
        LayoutRowScopeImpl(
            row = this@KeyRow,
            manager = this@KeyRow.manager,
            scope = this,
        ).content()
    }
}

@Composable
fun SenBoardLayout(
    slots: List<KeyData>,
    manager: SenBoardManager,
    content: @Composable LayoutColumnScopeImpl.() -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        LayoutColumnScopeImpl(
            slots = slots,
            manager = manager,
            scope = this,
        ).content()
    }
}
