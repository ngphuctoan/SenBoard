package banhmi.senboard.keyboard.model

import android.view.inputmethod.ExtractedTextRequest
import android.view.inputmethod.InputConnection
import banhmi.senboard.data.bigram.UserBigramViewModel
import banhmi.senboard.data.preferences.SenPreferences
import banhmi.senboard.data.preferences.SenPreferencesViewModel
import banhmi.senboard.engine.bigram.BigramEngine
import banhmi.senboard.keyboard.SenImService
import banhmi.senboard.keyboard.state.SenBoardState
import banhmi.senboard.keyboard.state.SenBoardStateViewModel
import banhmi.senboard.keyboard.state.ShiftMode
import banhmi.senboard.model.BigramResult
import banhmi.senboard.utils.EMPTY

object SenKeyHandlerContextDefaults {
    internal val NewSentenceDelimiters = listOf('.', '!', '?')
}

// The context will only provide important services, states, and methods to the handler
class SenKeyHandlerContext(
    // Handler should access inputConnection below instead
    imService: SenImService,
    val uiState: SenBoardState,
    val preferencesState: SenPreferences,
    // Kinda ugly to have to define all the state's view model's setters here
    val onUpdateModeType: (SenModeType) -> Unit,
    val onUpdateShiftMode: (ShiftMode) -> Unit,
    val onUpdateComposingText: (String) -> Unit,
    val onUpdateWordSuggestions: (List<BigramResult>) -> Unit,
    // For getting data from the bigram engine,
    val onGetClosestWords: (String) -> List<BigramResult>,
    val onGetBestCandidates: (String) -> List<BigramResult>,
) {
    // Is this still good code practice ;_;
    constructor(
        imService: SenImService,
        bigramEngine: BigramEngine,
        stateViewModel: SenBoardStateViewModel,
        preferencesViewModel: SenPreferencesViewModel,
        userBigramViewModel: UserBigramViewModel? = null,
    ) : this(
        imService = imService,
        uiState = stateViewModel.uiState.value,
        preferencesState = preferencesViewModel.preferencesState.value,
        onUpdateModeType = stateViewModel::updateModeType,
        onUpdateShiftMode = stateViewModel::updateShiftMode,
        onUpdateComposingText = stateViewModel::updateComposingText,
        onUpdateWordSuggestions = stateViewModel::updateWordSuggestions,
        onGetClosestWords = { text -> bigramEngine.getClosestWords(text, userProvidedBigramDataset = userBigramViewModel?.bigramDataset?.value) },
        onGetBestCandidates = { entryText -> bigramEngine.getBestCandidates(entryText, userProvidedBigramDataset = userBigramViewModel?.bigramDataset?.value) },
    )

    val inputConnection: InputConnection = imService.currentInputConnection

    // Clearing composing text is a common action
    fun clearComposingText() = onUpdateComposingText(String.EMPTY)

    // Not as common but still useful, and for consistency :b
    fun clearWordSuggestions() = onUpdateWordSuggestions(emptyList())

    // Used in both char and text key
    fun updateShiftModeAutomatically(
        preserveLastShiftMode: Boolean = false,
    ) = onUpdateShiftMode(
        when (uiState.shiftMode) {
            ShiftMode.CapsLocked -> ShiftMode.CapsLocked

            else if preferencesState.autoCapitalizationEnabled //
                    && isStartOfSentence() -> ShiftMode.Shifted

            else if preserveLastShiftMode -> uiState.shiftMode

            else -> ShiftMode.Off
        },
    )

    private fun isStartOfSentence(): Boolean {
        val extractedText = inputConnection.getExtractedText(ExtractedTextRequest(), 0)

        val textBeforeCursor = extractedText.text.substring(0, extractedText.selectionStart)
        if (textBeforeCursor.isBlank()) return true

        // We infer from isBlank() check that trimmed text always contains at least one non-whitespace characters
        val trimmedTextBeforeCursor = textBeforeCursor.trimEnd()
        val lastChar = trimmedTextBeforeCursor.last()
        // Additionally, we assume that trimEnd()'s trimmed part only contains whitespaces
        val whitespacesBeforeCursor = textBeforeCursor.substring(trimmedTextBeforeCursor.length)

        return lastChar in SenKeyHandlerContextDefaults.NewSentenceDelimiters
                // Only count as a new sentence when there is a whitespace after new sentence delimiters
                && whitespacesBeforeCursor.isNotEmpty()
    }
}

// By default, these handlers do nothing, so that implementations don't need to override all of them
interface SenKeyHandler {
    fun handleTap(
        context: SenKeyHandlerContext,
    ) {
    }

    fun handleDoubleTap(
        context: SenKeyHandlerContext,
    ) {
    }

    // This only works if the key's alternative is not defined, as it will override this!
    fun handleLongTap(
        context: SenKeyHandlerContext,
    ) {
    }
}