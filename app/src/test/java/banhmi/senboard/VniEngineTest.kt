package banhmi.senboard

import banhmi.senboard.engine.impl.VniEngine
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class VniEngineTest {

    @Test
    fun testVniConversion() {
        assertEquals("tiếng", VniEngine.convertWord("tieng61"))
        assertEquals("việt", VniEngine.convertWord("viet65"))
        assertEquals("đường", VniEngine.convertWord("duong972"))
        assertEquals("đông", VniEngine.convertWord("dong96"))
        assertEquals("kiểu", VniEngine.convertWord("kieu63"))
        assertEquals("kiểu", VniEngine.convertWord("kiêu3"))
        assertEquals("kiểu", VniEngine.convertWord("kieu36"))
        assertEquals("chuyển", VniEngine.convertWord("chuyen63"))
        assertEquals("đổi", VniEngine.convertWord("doi963"))
        assertEquals("tuyết", VniEngine.convertWord("tuyet61"))

        // Free position VNI digit typing (d9uong72 -> đường, d9em -> đem, d9e6m -> đêm, t6am -> tâm, so1ng6 -> sống)
        assertEquals("đường", VniEngine.convertWord("d9uong72"))
        assertEquals("đem", VniEngine.convertWord("d9em"))
        assertEquals("đêm", VniEngine.convertWord("d9e6m"))
        assertEquals("đêm", VniEngine.convertWord("d9em6"))
        assertEquals("tâm", VniEngine.convertWord("t6am"))
        assertEquals("sống", VniEngine.convertWord("so1ng6"))
        assertEquals("kiểu", VniEngine.convertWord("k6ieu3"))

        // Capitalization
        assertEquals("Đường", VniEngine.convertWord("Duong972"))
        assertEquals("Đường", VniEngine.convertWord("D9uong72"))
        assertEquals("Đông", VniEngine.convertWord("Dong96"))

        // Alphanumeric words protection (e.g. h2o, page1a, mp3player)
        assertEquals("mp3player", VniEngine.convertWord("mp3player"))
        assertEquals("page1a", VniEngine.convertWord("page1a"))

        // Repeated digit escape (a11 -> a1, a111 -> a11, a1111 -> a111, a22 -> a2, a33 -> a3, a66 -> a6, d999 -> d99, á1 -> a1)
        assertEquals("a1", VniEngine.convertWord("a11"))
        assertEquals("a11", VniEngine.convertWord("a111"))
        assertEquals("a111", VniEngine.convertWord("a1111"))
        assertEquals("a1111", VniEngine.convertWord("a11111"))
        assertEquals("a2", VniEngine.convertWord("a22"))
        assertEquals("a3", VniEngine.convertWord("a33"))
        assertEquals("a4", VniEngine.convertWord("a44"))
        assertEquals("a5", VniEngine.convertWord("a55"))
        assertEquals("a6", VniEngine.convertWord("a66"))
        assertEquals("a66", VniEngine.convertWord("a666"))
        assertEquals("o7", VniEngine.convertWord("o77"))
        assertEquals("a8", VniEngine.convertWord("a88"))
        assertEquals("d9", VniEngine.convertWord("d99"))
        assertEquals("d99", VniEngine.convertWord("d999"))
        assertEquals("d999", VniEngine.convertWord("d9999"))
        assertEquals("a1", VniEngine.convertWord("á1"))
        assertEquals("a2", VniEngine.convertWord("à2"))
        assertEquals("a3", VniEngine.convertWord("ả3"))
        assertEquals("a4", VniEngine.convertWord("ã4"))
        assertEquals("a5", VniEngine.convertWord("ạ5"))
        assertEquals("a6", VniEngine.convertWord("â6"))
    }
}
