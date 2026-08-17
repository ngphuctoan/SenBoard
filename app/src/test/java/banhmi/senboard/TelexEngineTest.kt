package banhmi.senboard

import banhmi.senboard.engine.impl.TelexEngine
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

        // No vowel stem & English word tests
        assertEquals("cvns", TelexEngine.convertWord("cvns"))
        assertEquals("cvnss", TelexEngine.convertWord("cvnss"))
        assertEquals("Cvnss", TelexEngine.convertWord("Cvnss"))
        assertEquals("telex", TelexEngine.convertWord("telex"))
        assertEquals("play", TelexEngine.convertWord("play"))
        assertEquals("print", TelexEngine.convertWord("print"))
        assertEquals("stop", TelexEngine.convertWord("stop"))
        assertEquals("skill", TelexEngine.convertWord("skill"))
        assertEquals("club", TelexEngine.convertWord("club"))

        // Delayed modifier vowel tests & Flexible keystroke order
        assertEquals("hôm", TelexEngine.convertWord("homo"))
        assertEquals("dêm", TelexEngine.convertWord("deme"))
        assertEquals("đêm", TelexEngine.convertWord("ddeme"))
        assertEquals("đêm", TelexEngine.convertWord("dedem"))
        assertEquals("đêm", TelexEngine.convertWord("ddeeem"))
        assertEquals("tâm", TelexEngine.convertWord("tama"))

        // 'w' after initial consonant tests
        assertEquals("sư", TelexEngine.convertWord("sw"))
        assertEquals("sứ", TelexEngine.convertWord("sws"))
        assertEquals("thư", TelexEngine.convertWord("thw"))

        // English words containing tone key letters followed by consonants
        assertEquals("confflict", TelexEngine.convertWord("confflict"))
        assertEquals("conflict", TelexEngine.convertWord("conflict"))
        assertEquals("confl", TelexEngine.convertWord("confl"))
        assertEquals("merrge", TelexEngine.convertWord("merrge"))
        assertEquals("merge", TelexEngine.convertWord("merge"))
        assertEquals("merg", TelexEngine.convertWord("merg"))
    }
}
