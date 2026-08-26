package banhmi.senboard

@Suppress("UNUSED")
class UserBigramStoreTest {

    //@BeforeEach
    //fun setUp() {
    //    BigramEngine.clear()
    //    UserBigramStore.clear()
    //
    //    val staticData = mapOf(
    //        "ngày" to listOf(
    //            BigramCandidate("mai", 100),
    //            BigramCandidate("hôm", 85),
    //            BigramCandidate("nay", 80),
    //        ),
    //    )
    //    BigramEngine.loadBigramsFromMap(staticData)
    //}
    //
    //@AfterEach
    //fun tearDown() {
    //    BigramEngine.clear()
    //    UserBigramStore.clear()
    //}
    //
    //@Test
    //fun testUserBigramRecordingAndPrediction() {
    //    // Initially static prediction
    //    var predictions = BigramEngine.predict("ngày")
    //    assertEquals(listOf("mai", "hôm", "nay"), predictions)
    //
    //    // User types "ngày" -> "gặp" twice
    //    UserBigramStore.recordBigram(null, "ngày", "gặp")
    //    UserBigramStore.recordBigram(null, "ngày", "gặp")
    //
    //    // User prediction "gặp" should be prioritized at top slot
    //    predictions = BigramEngine.predict("ngày")
    //    assertEquals("gặp", predictions[0])
    //    assertEquals("mai", predictions[1])
    //    assertEquals("hôm", predictions[2])
    //}
    //
    //@Test
    //fun testUserBigramFrequencySorting() {
    //    UserBigramStore.recordBigram(null, "ăn", "cơm")
    //    UserBigramStore.recordBigram(null, "ăn", "phở")
    //    UserBigramStore.recordBigram(null, "ăn", "phở")
    //
    //    val userPreds = UserBigramStore.getPredictions(null, "ăn")
    //    assertEquals("phở", userPreds[0])
    //    assertEquals("cơm", userPreds[1])
    //}
    //
    //@Test
    //fun testUserBigramDuplicateFiltering() {
    //    // User records "mai" for "ngày" (which is also in static)
    //    UserBigramStore.recordBigram(null, "ngày", "mai")
    //
    //    val predictions = BigramEngine.predict("ngày")
    //    // "mai" should not appear twice in the result
    //    assertEquals(3, predictions.size)
    //    assertEquals("mai", predictions[0])
    //    assertEquals("hôm", predictions[1])
    //    assertEquals("nay", predictions[2])
    //}
}
