package banhmi.senboard

import banhmi.senboard.ime.engine.VniEngine
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class VniEngineTest {

    @Test
    fun testVniConversion() {
        assertEquals("đường", VniEngine.convertWord("duong972"))
        assertEquals("tuyết", VniEngine.convertWord("tuyet61"))
        assertEquals("chợ", VniEngine.convertWord("cho75"))
        assertEquals("tiếng", VniEngine.convertWord("tieng61"))
        assertEquals("việt", VniEngine.convertWord("viet65"))
        assertEquals("đông", VniEngine.convertWord("dong96"))
        
        // Capitalization
        assertEquals("Đường", VniEngine.convertWord("Duong972"))
        assertEquals("Đông", VniEngine.convertWord("Dong96"))
        assertEquals("ĐƯỜNG", VniEngine.convertWord("DUONG972"))

        // Tone changing/overwriting
        assertEquals("á", VniEngine.convertWord("à1"))
        assertEquals("đường", VniEngine.convertWord("đương2"))
        assertEquals("chợ", VniEngine.convertWord("chơ5"))
        assertEquals("tiếng", VniEngine.convertWord("tiêng1"))
        assertEquals("vế", VniEngine.convertWord("về1"))
    }
}
