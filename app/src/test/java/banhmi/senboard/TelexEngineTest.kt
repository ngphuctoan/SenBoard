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

        // Free order typing for 'kiểu' (kieeur, kieure, kieuer, kierue, kiêur, kiêru)
        assertEquals("kiểu", TelexEngine.convertWord("kieeur"))
        assertEquals("kiểu", TelexEngine.convertWord("kiêur"))
        assertEquals("kiểu", TelexEngine.convertWord("kieure"))
        assertEquals("kiểu", TelexEngine.convertWord("kieuer"))
        assertEquals("kiểu", TelexEngine.convertWord("kierue"))
        assertEquals("kiểu", TelexEngine.convertWord("kiêru"))
        
        // Free order typing for 'chuyển' (chuyeern, chuyeerne)
        assertEquals("chuyển", TelexEngine.convertWord("chuyeern"))
        assertEquals("chuyển", TelexEngine.convertWord("chuyeerne"))

        // Order-Independent Telex typing (sống, đường, đêm, tấm, thường, sốn, són)
        assertEquals("sống", TelexEngine.convertWord("soosng"))
        assertEquals("sống", TelexEngine.convertWord("soongs"))
        assertEquals("sống", TelexEngine.convertWord("sosong"))
        assertEquals("sống", TelexEngine.convertWord("sosngo"))
        assertEquals("sống", TelexEngine.convertWord("songos"))
        assertEquals("sốn", TelexEngine.convertWord("soosn"))
        assertEquals("són", TelexEngine.convertWord("sosn"))
        assertEquals("sốn", TelexEngine.convertWord("sôsn"))

        assertEquals("đường", TelexEngine.convertWord("dduowngf"))
        assertEquals("đường", TelexEngine.convertWord("duongwfd"))
        assertEquals("đường", TelexEngine.convertWord("duowngdf"))

        assertEquals("đêm", TelexEngine.convertWord("dedem"))
        assertEquals("đêm", TelexEngine.convertWord("demde"))
        assertEquals("tấm", TelexEngine.convertWord("tasma"))
        assertEquals("thường", TelexEngine.convertWord("thowungf"))

        assertEquals("muốn", TelexEngine.convertWord("muoosn"))
        assertEquals("muón", TelexEngine.convertWord("muosn"))
        
        // Tone escape / undoing tone marks (é + s -> es, ess -> es, essc -> esc, esscape -> escape)
        assertEquals("es", TelexEngine.convertWord("és"))
        assertEquals("es", TelexEngine.convertWord("ess"))
        assertEquals("esc", TelexEngine.convertWord("essc"))
        assertEquals("escape", TelexEngine.convertWord("esscape"))
        assertEquals("af", TelexEngine.convertWord("aff"))
        assertEquals("sôngs", TelexEngine.convertWord("sốngs"))

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

        // English words protection
        assertEquals("cvns", TelexEngine.convertWord("cvns"))
        assertEquals("cvnss", TelexEngine.convertWord("cvnss"))
        assertEquals("Cvnss", TelexEngine.convertWord("Cvnss"))
        assertEquals("telex", TelexEngine.convertWord("telex"))
        assertEquals("play", TelexEngine.convertWord("play"))
        assertEquals("print", TelexEngine.convertWord("print"))
        assertEquals("stop", TelexEngine.convertWord("stop"))
        assertEquals("skill", TelexEngine.convertWord("skill"))
        assertEquals("club", TelexEngine.convertWord("club"))
        assertEquals("escape", TelexEngine.convertWord("escape"))
        assertEquals("status", TelexEngine.convertWord("status"))
        assertEquals("style", TelexEngine.convertWord("style"))
        assertEquals("special", TelexEngine.convertWord("special"))
        assertEquals("smart", TelexEngine.convertWord("smart"))
        assertEquals("snake", TelexEngine.convertWord("snake"))

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
        assertEquals("conflict", TelexEngine.convertWord("confflict"))
        assertEquals("conflict", TelexEngine.convertWord("conflict"))
        assertEquals("confl", TelexEngine.convertWord("confl"))
        assertEquals("merge", TelexEngine.convertWord("merrge"))
        assertEquals("merge", TelexEngine.convertWord("merge"))
        assertEquals("merg", TelexEngine.convertWord("merg"))
    }
}
