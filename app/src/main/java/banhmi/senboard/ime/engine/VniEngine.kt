package banhmi.senboard.ime.engine

import banhmi.senboard.engine.impl.VniEngine as ImplVniEngine

object VniEngine {
    fun convertWord(rawWord: String): String = ImplVniEngine.convertWord(rawWord)
}
