package banhmi.senboard.keyboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import banhmi.senboard.keyboard.keys.KeyData
import banhmi.senboard.keyboard.keys.KeyDisplay
import banhmi.senboard.keyboard.keys.KeyVariant
import banhmi.senboard.keyboard.keys.provideKeyStyle

interface LayoutScope {
    val slots: List<KeyData>
}

class LayoutColumnScopeImpl(
    override val slots: List<KeyData>,
    private val scope: ColumnScope,
) : LayoutScope, ColumnScope by scope {
    private var i = 0

    fun nextSlotIndex() = i++
}

class LayoutRowScopeImpl(
    private val row: LayoutColumnScopeImpl,
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
    val style = provideKeyStyle(variant)

    Box(
        modifier = Modifier
            .weight(weight)
            .fillMaxHeight(),
        contentAlignment = keyAlignment,
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(keyWeight / weight)
                .fillMaxHeight()
                .padding(8.dp),
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
                Box(modifier = Modifier.align(Alignment.TopEnd)) {
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

                        is KeyDisplay.None -> {}
                    }
                }
                when (slot.display) {
                    is KeyDisplay.Text -> Text(
                        text = slot.display.label,
                        style = style.typography.label,
                    )

                    is KeyDisplay.Icon -> Icon(
                        slot.display.icon,
                        contentDescription = null,
                        modifier = Modifier.size(style.iconSizes.main),
                    )

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
            scope = this,
        ).content()
    }
}

@Composable
fun SenBoardLayout(
    slots: List<KeyData>,
    content: @Composable LayoutColumnScopeImpl.() -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        LayoutColumnScopeImpl(
            slots = slots,
            scope = this,
        ).content()
    }
}
