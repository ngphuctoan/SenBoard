package banhmi.senboard.ime.keyboard.data.modes

import banhmi.senboard.ime.keyboard.models.Mode

enum class ModeId {
    Characters,
    Numerics,
}

val ModeId.value: Mode
    get() = when (this) {
        ModeId.Characters -> CharactersMode
        ModeId.Numerics -> NumericsMode
    }
