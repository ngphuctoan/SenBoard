package banhmi.senboard.ime.engine

import android.content.Context
import android.content.SharedPreferences
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object UserBigramStore {

    private const val PREFS_NAME = "senboard_user_bigrams"
    private const val KEY_DATA = "bigram_data"
    private const val MAX_PRIMARY_KEYS = 500

    private var memoryCache: MutableMap<String, MutableMap<String, Int>>? = null

    private fun getCache(context: Context?): MutableMap<String, MutableMap<String, Int>> {
        memoryCache?.let { return it }

        if (context == null) {
            val empty = mutableMapOf<String, MutableMap<String, Int>>()
            memoryCache = empty
            return empty
        }

        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val jsonString = prefs.getString(KEY_DATA, null)
        val cache = if (!jsonString.isNullOrEmpty()) {
            try {
                Json.decodeFromString<MutableMap<String, MutableMap<String, Int>>>(jsonString)
            } catch (e: Exception) {
                mutableMapOf()
            }
        } else {
            mutableMapOf()
        }
        memoryCache = cache
        return cache
    }

    /**
     * Record a user-typed bigram pair (prevWord -> nextWord).
     */
    fun recordBigram(context: Context?, rawPrev: String, rawNext: String) {
        val prev = rawPrev.trim().lowercase()
        val next = rawNext.trim().lowercase()

        if (prev.isBlank() || next.isBlank()) return
        if (prev.any { !it.isLetter() } || next.any { !it.isLetter() }) return

        val cache = getCache(context)
        val nextMap = cache.getOrPut(prev) { mutableMapOf() }
        val currentFreq = nextMap.getOrDefault(next, 0)
        nextMap[next] = currentFreq + 1

        // Evict older entries if cache size exceeds limit
        if (cache.size > MAX_PRIMARY_KEYS) {
            val keyToRemove = cache.keys.firstOrNull()
            if (keyToRemove != null) cache.remove(keyToRemove)
        }

        // Persist asynchronously
        if (context != null) {
            try {
                val jsonString = Json.encodeToString(cache)
                val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                prefs.edit().putString(KEY_DATA, jsonString).apply()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Returns predictions learned from this specific user for the given prevWord.
     */
    fun getPredictions(context: Context?, rawPrev: String): List<String> {
        val prev = rawPrev.trim().lowercase()
        if (prev.isBlank()) return emptyList()

        val cache = getCache(context)
        val nextMap = cache[prev] ?: return emptyList()

        return nextMap.entries
            .sortedByDescending { it.value }
            .map { it.key }
    }

    /**
     * Clear all user-learned data (useful for settings reset & unit testing).
     */
    fun clear(context: Context? = null) {
        memoryCache = mutableMapOf()
        if (context != null) {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            prefs.edit().remove(KEY_DATA).apply()
        }
    }

    /**
     * Directly seed cache for testing without needing Android Context.
     */
    internal fun seedForTesting(data: MutableMap<String, MutableMap<String, Int>>) {
        memoryCache = data
    }
}
