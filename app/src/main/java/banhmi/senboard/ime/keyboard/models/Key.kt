package banhmi.senboard.ime.keyboard.models

import androidx.compose.ui.Alignment

data class Key(
    val areaWeight: Float = 1f,
    val shapeWeight: Float = areaWeight,
    val shapeAlignment: Alignment = Alignment.CenterStart,
    val variant: KeyVariant = KeyVariant.Neutral,
)
