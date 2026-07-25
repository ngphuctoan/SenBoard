package banhmi.senboard

import banhmi.senboard.ime.engine.TelexEngine
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TelexEngineTest {

    @Test
    fun testTelexConversion() {
        assertEquals("tiếng", TelexEngine.convertWord("tieengs"))
        assertEquals("việt", TelexEngine.convertWord("vieetj"))
        assertEquals("đường", TelexEngine.convertWord("dduowngf"))
        assertEquals("đông", TelexEngine.convertWord("ddoong"))
        assertEquals("chuyển", TelexEngine.convertWord("chuyeern"))
        assertEquals("đổi", TelexEngine.convertWord("ddooir"))
        assertEquals("tuyết", TelexEngine.convertWord("tuyeets"))
        
        // Standalone 'w' rules
        assertEquals("hương", TelexEngine.convertWord("huongw"))
        assertEquals("tư", TelexEngine.convertWord("tuw"))
        assertEquals("mơ", TelexEngine.convertWord("mow"))
        assertEquals("ư", TelexEngine.convertWord("w"))
        assertEquals("ứ", TelexEngine.convertWord("ws"))
        assertEquals("ừ", TelexEngine.convertWord("wf"))
        assertEquals("ử", TelexEngine.convertWord("wr"))
        assertEquals("ữ", TelexEngine.convertWord("wx"))
        assertEquals("ự", TelexEngine.convertWord("wj"))
        assertEquals("Ư", TelexEngine.convertWord("W"))
        
        // Capitalization
        assertEquals("Đường", TelexEngine.convertWord("Dduowngf"))
        assertEquals("Đông", TelexEngine.convertWord("Ddoong"))

        // Tone changing/overwriting
        assertEquals("á", TelexEngine.convertWord("às"))
        assertEquals("đường", TelexEngine.convertWord("đươngf"))
        assertEquals("chợ", TelexEngine.convertWord("chơj"))
        assertEquals("tiếng", TelexEngine.convertWord("tiêngs"))
        assertEquals("vế", TelexEngine.convertWord("vềs"))
    }
}
