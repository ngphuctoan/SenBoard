package banhmi.senboard.ime.keyboard.models

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class KeyMargins(
    val horizontal: Dp = 4.dp,
    val vertical: Dp = 8.dp,
) {
    constructor(all: Dp) : this(all, all)

    fun getPaddingValues(): PaddingValues = PaddingValues(horizontal, vertical)
}
