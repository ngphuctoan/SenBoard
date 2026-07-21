package banhmi.senboard

import banhmi.senboard.ime.engine.CvnssEngine
import org.junit.Test
import java.io.File

class CvnssEngineTest {

    @Test
    fun testAllDictionaryEntries() {
        val dictFile = File("../dictionary.json")
        if (!dictFile.exists()) {
            println("dictionary.json not found at ${dictFile.absolutePath}")
            return
        }

        val content = dictFile.readText(Charsets.UTF_8)
        val regex = Regex("\"([^\"]+)\":\\s*\\{\\s*\"cvnss\":\\s*\"([^\"]+)\"")
        val matches = regex.findAll(content)

        var total = 0
        var passed = 0
        var failed = 0
        val failList = mutableListOf<String>()

        for (match in matches) {
            total++
            val expectedCqn = match.groupValues[1]
            val cvnssCode = match.groupValues[2]

            val actualCqn = CvnssEngine.convertWord(cvnssCode)

            if (actualCqn.equals(expectedCqn, ignoreCase = true)) {
                passed++
            } else {
                failed++
                if (failList.size < 50) {
                    failList.add("Code: '$cvnssCode' -> Actual: '$actualCqn', Expected: '$expectedCqn'")
                }
            }
        }

        val logFile = File("../test_results.txt")
        val sb = StringBuilder()
        sb.appendLine("=== CVNSS 4.0 TEST RESULTS ===")
        sb.appendLine("Total tested: $total")
        sb.appendLine("Passed: $passed")
        sb.appendLine("Failed: $failed")
        sb.appendLine("Pass rate: ${String.format("%.2f", passed.toDouble() / total * 100)}%")

        if (failList.isNotEmpty()) {
            sb.appendLine("\nFirst 100 Mismatches:")
            failList.take(100).forEach { sb.appendLine(it) }
        }

        logFile.writeText(sb.toString(), Charsets.UTF_8)
        println("Saved test results to ${logFile.absolutePath}")
    }
}
