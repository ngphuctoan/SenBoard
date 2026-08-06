package banhmi.senboard.ime.keyboard.data.modes

import banhmi.senboard.ime.keyboard.models.Mode

enum class ModeRegistry {
    Characters,
    Numerics,
    Symbolics,
}

val ModeRegistry.value: Mode
    get() = when (this) {
        ModeRegistry.Characters -> CharactersMode
        ModeRegistry.Numerics -> NumericsMode
        ModeRegistry.Symbolics -> SymbolicsMode
    }
