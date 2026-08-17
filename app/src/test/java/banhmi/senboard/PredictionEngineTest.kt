package banhmi.senboard

import banhmi.senboard.engine.bigram.BigramEngine
import banhmi.senboard.engine.bigram.BigramEntry
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class PredictionEngineTest {

    @BeforeEach
    fun setUp() {
        val testData = mapOf(
            "ngày" to listOf(
                BigramEntry("mai", 100),
                BigramEntry("hôm", 85),
                BigramEntry("nay", 80),
                BigramEntry("mốt", 70),
                BigramEntry("kia", 60)
            ),
            "tôi" to listOf(
                BigramEntry("đã", 100),
                BigramEntry("không", 95),
                BigramEntry("muốn", 85)
            ),
            "xin" to listOf(
                BigramEntry("chào", 100),
                BigramEntry("lỗi", 90),
                BigramEntry("cảm", 75)
            ),
            "việt" to listOf(
                BigramEntry("Nam", 100)
            )
        )
        BigramEngine.loadBigramsFromMap(testData)
    }

    @AfterEach
    fun tearDown() {
        BigramEngine.clear()
    }

    @Test
    fun testBasicPrediction() {
        val results = BigramEngine.predict("ngày")
        assertEquals(3, results.size)
        assertEquals("mai", results[0])
        assertEquals("hôm", results[1])
        assertEquals("nay", results[2])
    }

    @Test
    fun testPredictionSortedByFrequency() {
        val results = BigramEngine.predict("tôi")
        assertEquals("đã", results[0])
        assertEquals("không", results[1])
        assertEquals("muốn", results[2])
    }

    @Test
    fun testPredictionMaxResults() {
        val results = BigramEngine.predict("ngày", maxResults = 2)
        assertEquals(2, results.size)
    }

    @Test
    fun testPredictionCaseInsensitive() {
        val results = BigramEngine.predict("Ngày")
        assertEquals(3, results.size)
        assertEquals("mai", results[0])
    }

    @Test
    fun testPredictionUnknownWord() {
        val results = BigramEngine.predict("xyzabc")
        assertTrue(results.isEmpty())
    }

    @Test
    fun testPredictionEmptyInput() {
        val results = BigramEngine.predict("")
        assertTrue(results.isEmpty())
    }

    @Test
    fun testPredictionBlankInput() {
        val results = BigramEngine.predict("   ")
        assertTrue(results.isEmpty())
    }

    @Test
    fun testPredictionSingleResult() {
        val results = BigramEngine.predict("việt")
        assertEquals(1, results.size)
        assertEquals("Nam", results[0])
    }

    @Test
    fun testPredictionWithLeadingTrailingSpaces() {
        val results = BigramEngine.predict("  xin  ")
        assertEquals(3, results.size)
        assertEquals("chào", results[0])
    }

    @Test
    fun printDemoPredictions() {
        val inputs = listOf("ngày", "tôi", "xin", "việt")
        println("=== DEMO DỰ ĐOÁN TỪ TIẾP THEO (INPUT -> OUTPUT) ===")
        for (input in inputs) {
            val output = BigramEngine.predict(input)
            println("Input: \"$input\" -> Output (Gợi ý): $output")
        }
        println("==================================================")
    }
}
