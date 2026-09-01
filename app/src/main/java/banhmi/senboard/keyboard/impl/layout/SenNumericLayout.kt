package banhmi.senboard.keyboard.impl.layout

import banhmi.senboard.keyboard.model.SenLayoutProvider
import banhmi.senboard.keyboard.model.senLayout

val senNumericLayout: SenLayoutProvider = { _, _ ->
    senLayout {
        repeat(4) {
            senRow {
                repeat(4) { senKey() }
            }
        }
    }
}
