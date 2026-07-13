package banhmi.senboard.ime.keyboard.data.layouts

import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
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
            ),
        ),
        KeyRow(
            listOf(
                Key(1.5f, 1f, shapeAlignment = Alignment.CenterEnd),
                *Array(7) { Key() },
                Key(1.5f, 1f),
            ),
        ),
        KeyRow(
            listOf(
                Key(1.5f, variant = KeyVariant.Secondary),
                *Array(7) { Key() },
                Key(1.5f, variant = KeyVariant.Secondary),
            ),
        ),
        KeyRow(
            listOf(
                Key(1.5f, variant = KeyVariant.Tertiary),
                Key(variant = KeyVariant.Secondary),
                Key(variant = KeyVariant.Secondary),
                Key(4f),
                Key(variant = KeyVariant.Secondary),
                Key(1.5f, variant = KeyVariant.Primary),
            ),
        ),
    ),
)
