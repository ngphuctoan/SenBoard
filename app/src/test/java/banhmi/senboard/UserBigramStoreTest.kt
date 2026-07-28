package banhmi.senboard

import banhmi.senboard.ime.engine.BigramEntry
import banhmi.senboard.ime.engine.PredictionEngine
import banhmi.senboard.ime.engine.UserBigramStore
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UserBigramStoreTest {

    @BeforeEach
    fun setUp() {
        PredictionEngine.clear()
        UserBigramStore.clear()

        val staticData = mapOf(
            "ngày" to listOf(
                BigramEntry("mai", 100),
                BigramEntry("hôm", 85),
                BigramEntry("nay", 80)
            )
        )
        PredictionEngine.loadBigramsFromMap(staticData)
    }

    @AfterEach
    fun tearDown() {
        PredictionEngine.clear()
        UserBigramStore.clear()
    }

    @Test
    fun testUserBigramRecordingAndPrediction() {
        // Initially static prediction
        var predictions = PredictionEngine.predict("ngày")
        assertEquals(listOf("mai", "hôm", "nay"), predictions)

        // User types "ngày" -> "gặp" twice
        UserBigramStore.recordBigram(null, "ngày", "gặp")
        UserBigramStore.recordBigram(null, "ngày", "gặp")

        // User prediction "gặp" should be prioritized at top slot
        predictions = PredictionEngine.predict("ngày")
        assertEquals("gặp", predictions[0])
        assertEquals("mai", predictions[1])
        assertEquals("hôm", predictions[2])
    }

    @Test
    fun testUserBigramFrequencySorting() {
        UserBigramStore.recordBigram(null, "ăn", "cơm")
        UserBigramStore.recordBigram(null, "ăn", "phở")
        UserBigramStore.recordBigram(null, "ăn", "phở")

        val userPreds = UserBigramStore.getPredictions(null, "ăn")
        assertEquals("phở", userPreds[0])
        assertEquals("cơm", userPreds[1])
    }

    @Test
    fun testUserBigramDuplicateFiltering() {
        // User records "mai" for "ngày" (which is also in static)
        UserBigramStore.recordBigram(null, "ngày", "mai")

        val predictions = PredictionEngine.predict("ngày")
        // "mai" should not appear twice in the result
        assertEquals(3, predictions.size)
        assertEquals("mai", predictions[0])
        assertEquals("hôm", predictions[1])
        assertEquals("nay", predictions[2])
    }
}
