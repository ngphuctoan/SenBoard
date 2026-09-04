package banhmi.senboard.keyboard.model

import banhmi.senboard.data.preferences.SenPreferences
import banhmi.senboard.keyboard.impl.mode.senAaaaaMode
import banhmi.senboard.keyboard.impl.mode.senCharactersMode
import banhmi.senboard.keyboard.impl.mode.senExtraCharactersMode
import banhmi.senboard.keyboard.impl.mode.senExtraNumericMode
import banhmi.senboard.keyboard.impl.mode.senNumbersMode
import banhmi.senboard.keyboard.impl.mode.senNumericMode
import banhmi.senboard.keyboard.state.SenBoardState

data class SenMode(
    val layoutType: SenLayoutType,
    val keyDatas: List<SenKeyData>,
)

// Register the modes as enums here first before implementing them!
enum class SenModeType {
    Characters, Numbers, ExtraCharacters, Numeric, ExtraNumeric, Aaaaa;
}

fun provideMode(modeType: SenModeType) = when (modeType) {
    SenModeType.Characters -> senCharactersMode
    SenModeType.Numbers -> senNumbersMode
    SenModeType.ExtraCharacters -> senExtraCharactersMode
    SenModeType.Numeric -> senNumericMode
    SenModeType.ExtraNumeric -> senExtraNumericMode
    SenModeType.Aaaaa -> senAaaaaMode
}

typealias SenModeProvider = (SenBoardState, SenPreferences) -> SenMode
