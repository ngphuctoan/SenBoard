package banhmi.senboard.ime.keyboard.data.layouts

import banhmi.senboard.ime.keyboard.models.Key
import banhmi.senboard.ime.keyboard.models.KeyRow
import banhmi.senboard.ime.keyboard.models.KeyVariant
import banhmi.senboard.ime.keyboard.models.Layout

// Easter egg inspired by "aaaaa" by dkter
// App repo: https://github.com/dkter/aaaaa
val AaaaaLayout = Layout(
    name = "aaaaa",
    keyRows = listOf(
        KeyRow(
            listOf(
                Key(variant = KeyVariant.Ghost),
            ),
            heightWeight = 3f,
        ),
        KeyRow(
            listOf(
                Key(variant = KeyVariant.Ghost),
                Key(4f, variant = KeyVariant.Ghost),
                Key(variant = KeyVariant.Ghost),
            ),
        ),
    ),
)
