package banhmi.senboard.keyboard.layouts

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import banhmi.senboard.keyboard.Key
import banhmi.senboard.keyboard.KeyRow
import banhmi.senboard.keyboard.SenBoardManager
import banhmi.senboard.keyboard.SenBoardLayout
import banhmi.senboard.keyboard.keys.KeyData
import banhmi.senboard.keyboard.keys.KeyVariant

@Composable
fun Generic105Layout(slots: List<KeyData>, manager: SenBoardManager) {
    SenBoardLayout(slots = slots, manager = manager) {
        // First row
        KeyRow {
            repeat(10) { Key() }
            Key(variant = KeyVariant.NeutralRaised)
        }
        // Second row
        KeyRow {
            Key(1.5f, 1f, Alignment.CenterEnd)
            repeat(8) { Key() }
            Key(1.5f, variant = KeyVariant.Primary)
        }
        // Third row
        KeyRow {
            Key(variant = KeyVariant.NeutralRaised)
            repeat(9) { Key() }
            Key(variant = KeyVariant.NeutralRaised)
        }
        // Fourth row
        KeyRow {
            Key(variant = KeyVariant.NeutralRaised)
            Key(variant = KeyVariant.NeutralRaised)
            Key(7f)
            Key(variant = KeyVariant.NeutralRaised)
            Key(variant = KeyVariant.NeutralRaised)
        }
    }
}
