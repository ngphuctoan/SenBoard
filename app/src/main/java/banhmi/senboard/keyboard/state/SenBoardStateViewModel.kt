package banhmi.senboard.keyboard.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import banhmi.senboard.keyboard.model.SenModeType
import banhmi.senboard.utils.EMPTY
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SenBoardStateViewModel(
    private val modeType: SenModeType,
    private val shiftMode: ShiftMode,
    private val composingText: String,
    private val wordSuggestions: List<String>,
) : ViewModel() {
    // Factory to pass in the initial state values
    companion object {
        val MODE_TYPE_KEY = CreationExtras.Key<SenModeType>()

        val SHIFT_MODE_KEY = CreationExtras.Key<ShiftMode>()

        val COMPOSING_TEXT_KEY = CreationExtras.Key<String>()

        val WORD_SUGGESTIONS_KEY = CreationExtras.Key<List<String>>()

        val Factory = viewModelFactory {
            initializer {
                val modeType = this[MODE_TYPE_KEY] ?: SenBoardStateDefaults.DefaultModeType
                val shiftMode = this[SHIFT_MODE_KEY] ?: SenBoardStateDefaults.FallbackShiftMode
                val composingText = this[COMPOSING_TEXT_KEY] ?: String.EMPTY
                val wordSuggestions = this[WORD_SUGGESTIONS_KEY] ?: emptyList()
                SenBoardStateViewModel(modeType, shiftMode, composingText, wordSuggestions)
            }
        }
    }

    private val _uiState = MutableStateFlow(
        SenBoardState(
            modeType = modeType,
            shiftMode = shiftMode,
            composingText = composingText,
            wordSuggestions = wordSuggestions,
        ),
    )

    val uiState = _uiState.asStateFlow()

    fun updateModeType(newModeType: SenModeType) = _uiState.update { uiState ->
        uiState.copy(modeType = newModeType)
    }

    fun updateShiftMode(newShiftMode: ShiftMode) = _uiState.update { uiState ->
        uiState.copy(shiftMode = newShiftMode)
    }

    fun updateComposingText(newComposingText: String) = _uiState.update { uiState ->
        uiState.copy(composingText = newComposingText)
    }

    fun updateWordSuggestions(newWordSuggestions: List<String>) = _uiState.update { uiState ->
        uiState.copy(wordSuggestions = newWordSuggestions)
    }

    fun setState(newState: SenBoardState) = _uiState.update { newState }
}
