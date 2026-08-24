package banhmi.senboard.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset

// Self-explanatory, and it's for convenience :P
fun Offset.toIntOffset() = IntOffset(this.x.toInt(), this.y.toInt())
