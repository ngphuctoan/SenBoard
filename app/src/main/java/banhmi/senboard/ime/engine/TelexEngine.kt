package banhmi.senboard.ime.engine

import banhmi.senboard.engine.impl.TelexEngine as ImplTelexEngine

object TelexEngine {
    fun convertWord(rawWord: String): String = ImplTelexEngine.convertWord(rawWord)
}
