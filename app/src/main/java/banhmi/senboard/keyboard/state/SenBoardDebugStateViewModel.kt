package banhmi.senboard.keyboard.state

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.time.Duration

class SenBoardDebugStateViewModel(
    private val timeSinceTapDown: Duration?,
    private val deltaDurations: List<Duration>,
    private val lastTapPosition: Offset?,
    private val distances: List<Float>,
) : ViewModel() {
    companion object {
        val TIME_SINCE_TAP_DOWN_KEY = CreationExtras.Key<Duration?>()

        val DELTA_DURATIONS_KEY = CreationExtras.Key<List<Duration>>()

        val LAST_TAP_POSITION_KEY = CreationExtras.Key<Offset?>()

        val DISTANCES_KEY = CreationExtras.Key<List<Float>>()

        val Factory = viewModelFactory {
            initializer {
                val timeSinceTapDown = this[TIME_SINCE_TAP_DOWN_KEY]
                val deltaDurations = this[DELTA_DURATIONS_KEY] ?: emptyList()
                val lastTapPosition = this[LAST_TAP_POSITION_KEY]
                val distances = this[DISTANCES_KEY] ?: emptyList()
                SenBoardDebugStateViewModel(timeSinceTapDown, deltaDurations, lastTapPosition, distances)
            }
        }
    }

    private val _debugState = MutableStateFlow(
        SenBoardDebugState(
            timeSinceTapDown = timeSinceTapDown,
            deltaDurations = deltaDurations,
            lastTapPosition = lastTapPosition,
            distances = distances,
        ),
    )

    val debugState = _debugState.asStateFlow()

    fun updateTimeSinceTapDown(newTimeSinceTapDown: Duration?) = _debugState.update { debugState ->
        debugState.copy(timeSinceTapDown = newTimeSinceTapDown)
    }

    fun updateDeltaDurations(newDeltaDurations: List<Duration>) = _debugState.update { debugState ->
        debugState.copy(deltaDurations = newDeltaDurations)
    }

    fun updateLastTapPosition(newLastTapPosition: Offset?) = _debugState.update { debugState ->
        debugState.copy(lastTapPosition = newLastTapPosition)
    }

    fun updateDistances(newDistances: List<Float>) = _debugState.update { debugState ->
        debugState.copy(distances = newDistances)
    }
}