package banhmi.senboard.utils

import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

fun countOccurences(
    text: String,
): Map<Char, Int> =
    text.fold(initial = emptyMap()) { charOccurences, char ->
        val previousOccurences = charOccurences.getOrDefault(char, 0)
        charOccurences.plus(char to previousOccurences + 1)
    }

fun <T : Number> cosineSimilarity(
    first: List<T>,
    second: List<T>,
): Float {
    val minimumLastIndex = min(first.lastIndex, second.lastIndex)

    val dotProduct = (0..minimumLastIndex).fold(initial = 0f) { dot, index ->
        dot + first[index].toFloat() * second[index].toFloat()
    }

    val normalizedFirst = sqrt(
        (0..minimumLastIndex).fold(initial = 0f) { magnitude, index ->
            magnitude + first[index].toFloat().pow(2)
        },
    )
    val normalizedSecond = sqrt(
        (0..minimumLastIndex).fold(initial = 0f) { magnitude, index ->
            magnitude + second[index].toFloat().pow(2)
        },
    )

    return dotProduct / (normalizedFirst * normalizedSecond)
}
