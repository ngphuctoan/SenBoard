package banhmi.senboard.ime.keyboard.data.layouts

import banhmi.senboard.ime.keyboard.dsl.layout
import banhmi.senboard.ime.keyboard.models.KeyVariant

// Easter egg inspired by "aaaaa" by dkter
// App repo: https://github.com/dkter/aaaaa
val AaaaaLayout = layout("aaaaa") {
    row(heightWeight = 3f) {
        key(variant = KeyVariant.Ghost, forceHighlightState = false)
    }
    row {
        key(variant = KeyVariant.Ghost, forceHighlightState = false)
        key(areaWeight = 4f, variant = KeyVariant.Ghost, forceHighlightState = false)
        key(variant = KeyVariant.Ghost, forceHighlightState = false)
    }
}
