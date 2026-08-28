package banhmi.senboard

@Suppress("UNUSED")
class PredictionEngineTest {

    //@BeforeEach
    //fun setUp() {
    //    val testData = mapOf(
    //        "ngày" to listOf(
    //            BigramCandidate("mai", 100),
    //            BigramCandidate("hôm", 85),
    //            BigramCandidate("nay", 80),
    //            BigramCandidate("mốt", 70),
    //            BigramCandidate("kia", 60),
    //        ),
    //        "tôi" to listOf(
    //            BigramCandidate("đã", 100),
    //            BigramCandidate("không", 95),
    //            BigramCandidate("muốn", 85),
    //        ),
    //        "xin" to listOf(
    //            BigramCandidate("chào", 100),
    //            BigramCandidate("lỗi", 90),
    //            BigramCandidate("cảm", 75),
    //        ),
    //        "việt" to listOf(
    //            BigramCandidate("Nam", 100),
    //        ),
    //    )
    //    BigramEngine.loadBigramsFromMap(testData)
    //}
    //
    //@AfterEach
    //fun tearDown() {
    //    BigramEngine.clear()
    //}
    //
    //@Test
    //fun testBasicPrediction() {
    //    val results = BigramEngine.predict("ngày")
    //    assertEquals(3, results.size)
    //    assertEquals("mai", results[0])
    //    assertEquals("hôm", results[1])
    //    assertEquals("nay", results[2])
    //}
    //
    //@Test
    //fun testPredictionSortedByFrequency() {
    //    val results = BigramEngine.predict("tôi")
    //    assertEquals("đã", results[0])
    //    assertEquals("không", results[1])
    //    assertEquals("muốn", results[2])
    //}
    //
    //@Test
    //fun testPredictionMaxResults() {
    //    val results = BigramEngine.predict("ngày", maxResults = 2)
    //    assertEquals(2, results.size)
    //}
    //
    //@Test
    //fun testPredictionCaseInsensitive() {
    //    val results = BigramEngine.predict("Ngày")
    //    assertEquals(3, results.size)
    //    assertEquals("mai", results[0])
    //}
    //
    //@Test
    //fun testPredictionUnknownWord() {
    //    val results = BigramEngine.predict("xyzabc")
    //    assertTrue(results.isEmpty())
    //}
    //
    //@Test
    //fun testPredictionEmptyInput() {
    //    val results = BigramEngine.predict("")
    //    assertTrue(results.isEmpty())
    //}
    //
    //@Test
    //fun testPredictionBlankInput() {
    //    val results = BigramEngine.predict("   ")
    //    assertTrue(results.isEmpty())
    //}
    //
    //@Test
    //fun testPredictionSingleResult() {
    //    val results = BigramEngine.predict("việt")
    //    assertEquals(1, results.size)
    //    assertEquals("Nam", results[0])
    //}
    //
    //@Test
    //fun testPredictionWithLeadingTrailingSpaces() {
    //    val results = BigramEngine.predict("  xin  ")
    //    assertEquals(3, results.size)
    //    assertEquals("chào", results[0])
    //}
    //
    //@Test
    //fun printDemoPredictions() {
    //    val inputs = listOf("ngày", "tôi", "xin", "việt")
    //    println("=== DEMO DỰ ĐOÁN TỪ TIẾP THEO (INPUT -> OUTPUT) ===")
    //    for (input in inputs) {
    //        val output = BigramEngine.predict(input)
    //        println("Input: \"$input\" -> Output (Gợi ý): $output")
    //    }
    //    println("==================================================")
    //}
}
