package banhmi.senboard.keyboard.impl.layout

import androidx.compose.ui.Alignment
import banhmi.senboard.keyboard.model.SenLayoutProvider
import banhmi.senboard.keyboard.model.senLayout

val senStandardLayout: SenLayoutProvider = { _, preferences ->
    senLayout {
        if (preferences.numberRowEnabled) {
            senRow {
                repeat(10) { senKey() }
            }
        }
        senRow {
            repeat(10) { senKey() }
        }
        senRow {
            senKey(1.5f, 1f, Alignment.End)
            repeat(7) { senKey() }
            senKey(1.5f, 1f)
        }
        senRow {
            senKey(1.5f, 1.25f)
            repeat(7) { senKey() }
            senKey(1.5f, 1.25f, Alignment.End)
        }
        senRow {
            senKey(1.5f)
            repeat(2) { senKey() }
            senKey(4f)
            senKey()
            senKey(1.5f)
        }
    }
}
