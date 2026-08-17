package banhmi.senboard.keyboard.impl.mode

import banhmi.senboard.keyboard.impl.layout.SenAaaaaLayout
import banhmi.senboard.keyboard.model.senBackSpaceKey
import banhmi.senboard.keyboard.model.senMode
import banhmi.senboard.keyboard.model.senReturnKey
import banhmi.senboard.keyboard.model.senSpaceKey
import banhmi.senboard.keyboard.model.senTextKey

val SenAaaaaMode = senMode(SenAaaaaLayout) {
    // First row
    senTextKey("a") // Make this a text key to bypass engine conversion

    // Second row
    senBackSpaceKey()
    senSpaceKey()
    senReturnKey()
}
