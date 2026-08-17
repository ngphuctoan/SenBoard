package banhmi.senboard

import banhmi.senboard.engine.impl.CvnssEngine
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.io.File
import java.util.stream.Stream

@Serializable
data class DictEntry(
    val cvnss: String,
    val definition: String = ""
)

@Serializable
data class DictionaryRoot(
    val dictionary: Map<String, DictEntry>
)

class CvnssEngineTest {

    companion object {
        @JvmStatic
        fun provideDictionaryEntries(): Stream<Arguments> {
            val file = File("../dictionary.json")
            if (!file.exists()) {
                println("Không tìm thấy tệp dictionary.json tại: ${file.absolutePath}")
                return Stream.empty()
            }
            val content = file.readText(Charsets.UTF_8)
            val json = Json { ignoreUnknownKeys = true }
            val root = json.decodeFromString<DictionaryRoot>(content)

            return root.dictionary.entries
                .stream()
                .map { entry ->
                    Arguments.of(entry.value.cvnss, entry.key)
                }
        }
    }

    @ParameterizedTest(name = "{index} => Chữ Việt Nhanh: {0} -> Quốc Ngữ: {1}")
    @MethodSource("provideDictionaryEntries")
    fun testConversion(cvnssInput: String, expectedCqn: String) {
        val actual = java.text.Normalizer.normalize(
            CvnssEngine.convertWord(cvnssInput),
            java.text.Normalizer.Form.NFC
        )
        val expected = java.text.Normalizer.normalize(expectedCqn, java.text.Normalizer.Form.NFC)
        if (actual != expected) {
            println(
                "MISMATCH: Code: '$cvnssInput' -> Actual: '$actual' (${
                    actual.map {
                        it.code.toString(
                            16
                        )
                    }
                }), Expected: '$expected' (${expected.map { it.code.toString(16) }})"
            )
        }
        assertEquals(expected, actual)
    }

    @org.junit.jupiter.api.Test
    fun testCvnssEdgeCases() {
        assertEquals("tuyết", CvnssEngine.convertWord("tydb"))
        assertEquals("Tuyết", CvnssEngine.convertWord("Tydb"))
        assertEquals("TUYẾT", CvnssEngine.convertWord("TYDB"))
    }
}
