package banhmi.senboard.keyboard.state

import androidx.compose.ui.geometry.Offset
import kotlin.time.Duration

// Too lazy to do Result interface so nullable :b
data class SenBoardDebugState(
    val timeSinceTapDown: Duration? = null,
    val deltaDurations: List<Duration> = emptyList(),
    val lastTapPosition: Offset? = null,
    val distances: List<Float> = emptyList(),
)
