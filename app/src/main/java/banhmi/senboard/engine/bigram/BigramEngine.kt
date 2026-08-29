package banhmi.senboard.engine.bigram

import android.content.Context
import banhmi.senboard.model.BigramDataset
import banhmi.senboard.model.BigramDatasetTakeLimit
import banhmi.senboard.model.BigramEntry
import banhmi.senboard.model.BigramResult
import banhmi.senboard.model.addAllFrom
import kotlinx.serialization.json.Json

private const val DATASET_FILE_NAME = "bigrams.json"

object BigramEngineDefaults {
    @JvmStatic
    val TakeLimit = BigramDatasetTakeLimit.Max(3)
}

class BigramEngine(
    private val context: Context,
) {
    private val bigramDataset = BigramDataset(entries = mutableListOf())

    private val jsonWithUnknownKeys = Json { ignoreUnknownKeys = true }

    // Remember to clear first before loading
    fun clearDataset(): BigramEngine {
        bigramDataset.entries.clear()
        return this // Makes it easier to chain clearDataset().loadDataset()
    }

    fun loadDataset(): BigramEngine {
        if (bigramDataset.entries.isNotEmpty()) throw IllegalStateException(
            "Dataset must be cleared before loading. Have you called 'clearDataset()'?",
        )

        context.assets.open(DATASET_FILE_NAME).bufferedReader().readText().let { encodedJson ->
            val otherBigramDataset: BigramDataset<List<BigramEntry>> = jsonWithUnknownKeys.decodeFromString(encodedJson)
            bigramDataset.addAllFrom(otherBigramDataset)
        }

        return this
    }

    fun <T : List<BigramEntry>> getClosestWords(
        text: String,
        takeLimit: BigramDatasetTakeLimit = BigramEngineDefaults.TakeLimit,
        // Merge the dataset with user provided one (e.g. from a DataStore)
        userProvidedBigramDataset: BigramDataset<T>? = null,
    ): List<BigramResult> {
        val mergedBigramDataset = if (userProvidedBigramDataset != null) {
            bigramDataset + userProvidedBigramDataset
        } else {
            bigramDataset
        }

        return mergedBigramDataset.getClosestWords(text, takeLimit)
    }

    fun <T : List<BigramEntry>> getBestCandidates(
        entryText: String,
        takeLimit: BigramDatasetTakeLimit = BigramEngineDefaults.TakeLimit,
        userProvidedBigramDataset: BigramDataset<T>? = null,
    ): List<BigramResult> {
        val mergedBigramDataset = if (userProvidedBigramDataset != null) {
            bigramDataset + userProvidedBigramDataset
        } else {
            bigramDataset
        }

        return mergedBigramDataset.getBestCandidates(entryText, takeLimit)
    }
}
