package banhmi.senboard.ime.keyboard.data.layouts

import androidx.compose.ui.Alignment
import banhmi.senboard.ime.keyboard.models.Key
import banhmi.senboard.ime.keyboard.models.KeyRow
import banhmi.senboard.ime.keyboard.models.KeyVariant
import banhmi.senboard.ime.keyboard.models.Layout

val StandardLayout = Layout(
    name = "standard",
    keyRows = listOf(
        KeyRow(
            listOf(
                *Array(10) { Key() },
                Key(variant = KeyVariant.NeutralRaised),
            ),
        ),
        KeyRow(
            listOf(
                Key(1.5f, 1f, shapeAlignment = Alignment.CenterEnd),
                *Array(8) { Key() },
                Key(1.5f, variant = KeyVariant.Primary),
            ),
        ),
        KeyRow(
            listOf(
                Key(variant = KeyVariant.NeutralRaised),
                *Array(9) { Key() },
                Key(variant = KeyVariant.NeutralRaised),
            ),
        ),
        KeyRow(
            listOf(
                Key(variant = KeyVariant.NeutralRaised),
                Key(variant = KeyVariant.NeutralRaised),
                Key(7f),
                Key(variant = KeyVariant.NeutralRaised),
                Key(variant = KeyVariant.NeutralRaised),
            ),
        ),
    ),
)
