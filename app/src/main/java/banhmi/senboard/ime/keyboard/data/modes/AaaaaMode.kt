package banhmi.senboard.ime.keyboard.data.modes

import banhmi.senboard.ime.keyboard.data.layouts.AaaaaLayout
import banhmi.senboard.ime.keyboard.dsl.mode

// Easter egg inspired by "aaaaa" by dkter
// App repo: https://github.com/dkter/aaaaa
val AaaaaMode = mode("aaaaa", AaaaaLayout) {
    charKey("a")
    backspaceKey()
    spaceKey()
    returnKey()
}
