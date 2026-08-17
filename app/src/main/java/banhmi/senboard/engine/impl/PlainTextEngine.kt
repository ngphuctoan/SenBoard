package banhmi.senboard.engine.impl

import banhmi.senboard.engine.VietnameseEngine

// A reference engine where it returns the exact composing text
object PlainTextEngine : VietnameseEngine {
    override fun convertWord(rawWord: String): String = rawWord
}
