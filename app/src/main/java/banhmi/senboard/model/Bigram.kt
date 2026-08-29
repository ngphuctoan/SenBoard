package banhmi.senboard.model

import banhmi.senboard.utils.cosineSimilarity
import banhmi.senboard.utils.countOccurences
import banhmi.senboard.utils.prepended
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import banhmi.senboard.datastore.snippets.proto.BigramCandidate as ProtoBigramCandidate
import banhmi.senboard.datastore.snippets.proto.BigramEntry as ProtoBigramEntry
import banhmi.senboard.datastore.snippets.proto.UserBigram as ProtoUserBigram

@Serializable
data class BigramCandidate(
    @SerialName("word") val text: String,
    @SerialName("freq") val frequency: Float,
)

// Convert DataStore Protobuf's class to this app domain's data class
fun ProtoBigramCandidate.toAppDomain() = BigramCandidate(
    text = this.text,
    frequency = this.frequency,
)

@Serializable
data class BigramEntry(
    val text: String,
    val candidates: List<BigramCandidate>,
)

// Ditto
fun ProtoBigramEntry.toAppDomain() = BigramEntry(
    text = this.text,
    candidates = this.candidatesList.map { candidate ->
        candidate.toAppDomain()
    },
)

sealed interface BigramDatasetTakeLimit {
    object Unlimited : BigramDatasetTakeLimit

    data class Max(val value: Int) : BigramDatasetTakeLimit
}

data class BigramResult(
    val text: String,
    // Only used for the original text included in closest words
    val isOriginal: Boolean = false,
)

// We want to allow for MutableList entries to be typed as well
@Serializable(BigramDatasetSerializer::class)
data class BigramDataset<T : List<BigramEntry>>(
    val entries: T,
) {
    // Merge two datasets into one
    operator fun <V : List<BigramEntry>> plus(other: BigramDataset<V>) = BigramDataset(
        entries = this.entries + other.entries,
    )

    /* This should be used to find similar words given our specified word
    (NOT based on semantics, but number of letter occurrences) */
    fun getClosestWords(
        text: String,
        takeLimit: BigramDatasetTakeLimit,
    ): List<BigramResult> {
        val text = text.trim().lowercase()

        if (text.isBlank()) return emptyList()

        return entries
            .flatMap { entry ->
                entry.candidates
                    .map { candidate -> BigramResult(candidate.text) }
                    // Don't forget to include entry text!
                    .plus(BigramResult(entry.text))
            }
            .distinct()
            // Candidate text equal to text should be removed, as we will re-include it later
            .filter { candidate -> candidate.text.startsWith(text) && candidate.text != text }
            .sortedByDescending { candidate ->
                cosineSimilarity(
                    countOccurences(candidate.text).values.toList(),
                    countOccurences(text).values.toList(),
                )
            }
            // Also include original text, from my observation iOS shows it at the beginning with quotes
            .prepended(BigramResult(text, isOriginal = true))
            .let { candidates ->
                when (takeLimit) {
                    is BigramDatasetTakeLimit.Max -> candidates.take(takeLimit.value)
                    BigramDatasetTakeLimit.Unlimited -> candidates
                }
            }
    }

    // This should be used for guessing the next word based on "semantics" (i.e. bigrams)
    fun getBestCandidates(
        entryText: String,
        takeLimit: BigramDatasetTakeLimit,
    ): List<BigramResult> {
        val entryText = entryText.trim().lowercase()

        if (entryText.isBlank()) return emptyList()

        return entries
            .filter { entry -> entry.text.startsWith(entryText) }
            .flatMap { entry -> entry.candidates }
            .sortedByDescending { candidate -> candidate.frequency }
            .map { candidate -> BigramResult(candidate.text) }
            .let { candidates ->
                when (takeLimit) {
                    is BigramDatasetTakeLimit.Max -> candidates.take(takeLimit.value)
                    BigramDatasetTakeLimit.Unlimited -> candidates
                }
            }
    }
}

// Ditto, again
fun ProtoUserBigram.toAppDomain() = BigramDataset(
    entries = this.entriesList.map { entry ->
        entry.toAppDomain()
    },
)

// Special extension function for dataset with mutable list to add all entries from another dataset
fun <T : List<BigramEntry>> BigramDataset<MutableList<BigramEntry>>.addAllFrom(
    other: BigramDataset<T>,
) = this.entries.addAll(other.entries)

// This (de)serializes JSON map (entry text -> candidates) from/to list of BigramEntry
object BigramDatasetSerializer : KSerializer<BigramDataset<List<BigramEntry>>> {
    private val delegateSerializer = MapSerializer(
        keySerializer = String.serializer(),
        valueSerializer = ListSerializer(BigramCandidate.serializer()),
    )

    override val descriptor: SerialDescriptor
        get() = SerialDescriptor(
            "banhmi.senboard.model.BigramDataset",
            delegateSerializer.descriptor,
        )

    override fun deserialize(decoder: Decoder) = BigramDataset(
        entries = decoder.decodeSerializableValue(delegateSerializer).map { (text, candidates) ->
            BigramEntry(text, candidates)
        },
    )

    override fun serialize(
        encoder: Encoder,
        value: BigramDataset<List<BigramEntry>>,
    ) = encoder.encodeSerializableValue(
        delegateSerializer,
        value.entries.associateBy(
            { entry -> entry.text },
            { entry -> entry.candidates },
        ),
    )
}
