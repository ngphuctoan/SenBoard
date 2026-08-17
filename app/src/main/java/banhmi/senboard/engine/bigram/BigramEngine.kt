package banhmi.senboard.engine.bigram

import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class BigramEntry(val word: String, val freq: Int)

object BigramEngine {

    private var bigramMap: Map<String, List<BigramEntry>> = emptyMap()
    private var isLoaded = false

    /**
     * Load bigram data from assets/bigrams.json.
     * Should be called once when the keyboard service initializes.
     */
    fun loadBigrams(context: Context) {
        if (isLoaded) return
        try {
            val jsonString = context.assets
                .open("bigrams.json")
                .bufferedReader()
                .use { it.readText() }
            val json = Json { ignoreUnknownKeys = true }
            bigramMap = json.decodeFromString<Map<String, List<BigramEntry>>>(jsonString)
            isLoaded = true
        } catch (e: Exception) {
            e.printStackTrace()
            bigramMap = emptyMap()
        }
    }

    /**
     * Returns top N predicted next words based on the previous word.
     * Prioritizes user-learned predictions from UserBigramStore, filling remaining slots from static bigramMap.
     */
    fun predict(previousWord: String, context: Context? = null, maxResults: Int = 3): List<String> {
        if (previousWord.isBlank()) return emptyList()

        val key = previousWord
            .trim()
            .lowercase()
        val resultList = mutableListOf<String>()
        val seen = mutableSetOf<String>()

        // 1. Prioritize user-personalized predictions
        val userPredictions = UserBigramStore.getPredictions(context, key)
        for (w in userPredictions) {
            if (seen.add(w.lowercase())) {
                resultList.add(w)
                if (resultList.size >= maxResults) return resultList
            }
        }

        // 2. Fallback / fill remaining from static bigram dataset
        if (isLoaded) {
            val entries = bigramMap[key] ?: emptyList()
            val staticPredictions = entries
                .sortedByDescending { it.freq }
                .map { it.word }

            for (w in staticPredictions) {
                if (seen.add(w.lowercase())) {
                    resultList.add(w)
                    if (resultList.size >= maxResults) return resultList
                }
            }
        }

        return resultList
    }

    /**
     * For unit testing: load bigrams directly from a map without needing Context.
     */
    fun loadBigramsFromMap(data: Map<String, List<BigramEntry>>) {
        bigramMap = data
        isLoaded = true
    }

    /**
     * Clear loaded data (useful for testing).
     */
    fun clear() {
        bigramMap = emptyMap()
        isLoaded = false
        UserBigramStore.clear()
    }
}
