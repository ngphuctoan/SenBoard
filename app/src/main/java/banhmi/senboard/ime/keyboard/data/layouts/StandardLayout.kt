package banhmi.senboard.ime.keyboard.data.layouts

import androidx.compose.ui.Alignment
import banhmi.senboard.ime.keyboard.dsl.layout
import banhmi.senboard.ime.keyboard.models.KeyVariant

val StandardLayout = layout("standard") {
    row {
        keys(10)
    }
    row {
        key(areaWeight = 1.5f, shapeWeight = 1f, shapeAlignment = Alignment.CenterEnd)
        keys(7)
        key(areaWeight = 1.5f, shapeWeight = 1f)
    }
    row {
        key(areaWeight = 1.5f, variant = KeyVariant.Secondary)
        keys(7)
        key(areaWeight = 1.5f, variant = KeyVariant.Secondary)
    }
    row {
        key(areaWeight = 1.5f, variant = KeyVariant.Tertiary)
        key(variant = KeyVariant.Secondary)
        key(variant = KeyVariant.Secondary)
        key(areaWeight = 4f)
        key(variant = KeyVariant.Secondary)
        key(areaWeight = 1.5f, variant = KeyVariant.Primary)
    }
}
