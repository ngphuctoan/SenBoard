package banhmi.senboard.keyboard.model

import banhmi.senboard.keyboard.impl.mode.SenAaaaaMode
import banhmi.senboard.keyboard.impl.mode.SenCharactersMode
import banhmi.senboard.keyboard.impl.mode.SenExtraCharactersMode
import banhmi.senboard.keyboard.impl.mode.SenNumbersMode

data class SenMode(
    val layout: SenLayout,
    val keyDatas: List<SenKeyData>,
)

// Register the modes as enums here first before implementing them!
enum class SenModeType {
    Characters, Numbers, ExtraCharacters, Aaaaa;
}

fun provideMode(modeType: SenModeType): SenMode = when (modeType) {
    SenModeType.Characters -> SenCharactersMode
    SenModeType.Numbers -> SenNumbersMode
    SenModeType.ExtraCharacters -> SenExtraCharactersMode
    SenModeType.Aaaaa -> SenAaaaaMode
}
