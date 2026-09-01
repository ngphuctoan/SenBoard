package banhmi.senboard.keyboard.state

import android.text.InputType
import banhmi.senboard.keyboard.model.SenModeType
import banhmi.senboard.model.BigramResult
import banhmi.senboard.utils.EMPTY

enum class ShiftMode {
    Off, Shifted, CapsLocked;
}

// These are the initial values of the state
object SenBoardStateDefaults {
    val DefaultModeType = SenModeType.Characters

    val FallbackShiftMode: ShiftMode = ShiftMode.Off

    fun shiftMode(autoCapitalizationEnabled: Boolean): ShiftMode =
        if (autoCapitalizationEnabled) ShiftMode.Shifted else FallbackShiftMode

    @JvmStatic
    val DefaultInputType = InputType.TYPE_CLASS_TEXT
}

data class SenBoardState(
    val modeType: SenModeType = SenBoardStateDefaults.DefaultModeType,
    val shiftMode: ShiftMode = SenBoardStateDefaults.FallbackShiftMode,
    val composingText: String = String.EMPTY,
    val wordSuggestions: List<BigramResult> = emptyList(),
    val inputType: Int = SenBoardStateDefaults.DefaultInputType,
) {
    val isShifted
        get() = shiftMode == ShiftMode.Shifted || shiftMode == ShiftMode.CapsLocked

    /* Only allow composing (including engine conversion, setting composing text)
    on specific text input type varations (i.e. not allowing for other input types such as
    numbers or datetimes, or text variations such as passwords or email addresses) */
    val inputTypeComposingAllowed
        get() = listOf(
            InputType.TYPE_TEXT_VARIATION_FILTER,
            InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE,
            InputType.TYPE_TEXT_VARIATION_NORMAL,
            InputType.TYPE_TEXT_VARIATION_PERSON_NAME,
            InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS,
            InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE,
            InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT,
        ) //
            .map { variation -> InputType.TYPE_CLASS_TEXT or variation } //
            .any { inputType ->
                this.inputType and (InputType.TYPE_MASK_CLASS //
                        or InputType.TYPE_MASK_VARIATION) == inputType
            }
}
