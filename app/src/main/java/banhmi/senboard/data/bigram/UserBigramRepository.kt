package banhmi.senboard.data.bigram

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import banhmi.senboard.datastore.snippets.proto.bigramCandidate
import banhmi.senboard.datastore.snippets.proto.bigramEntry
import banhmi.senboard.model.toAppDomain
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import banhmi.senboard.datastore.snippets.proto.UserBigram as ProtoUserBigram

class UserBigramRepository @Inject constructor(
    private val dataStore: DataStore<ProtoUserBigram>,
) {
    val userBigramFlow = dataStore.data.catch { exception ->
        when (exception) {
            // Tell the consumer that the bigrams are empty when failed to get the data store
            is IOException -> emit(UserBigramSerializer.defaultValue)
            else -> throw exception
        }
    }.map { userBigram ->
        userBigram.toAppDomain()
    }

    suspend fun saveBigram(
        entryText: String,
        candidateText: String,
        incrementWeight: Float,
    ) {
        val entryText = entryText.trim().lowercase()
        if (entryText.isBlank() || entryText.any { char -> !char.isLetter() }) return

        val candidateText = candidateText.trim().lowercase()
        if (candidateText.isBlank() || candidateText.any { char -> !char.isLetter() }) return

        dataStore.updateData { userBigram ->
            for (entryIndex in 0..<userBigram.entriesCount) {
                val existingEntry = userBigram.getEntries(entryIndex)

                if (existingEntry != null && existingEntry.text == entryText) {
                    for (candidateIndex in 0..<existingEntry.candidatesCount) {
                        val existingCandidate = existingEntry.getCandidates(candidateIndex)

                        if (existingCandidate != null && existingCandidate.text == candidateText) {
                            val frequency = existingCandidate.frequency + incrementWeight

                            return@updateData userBigram.toBuilder().setEntries(
                                entryIndex,
                                existingEntry.toBuilder().setCandidates(
                                    candidateIndex,
                                    existingCandidate.toBuilder().setFrequency(frequency).build(),
                                ).build(),
                            ).build()
                        }
                    }

                    return@updateData userBigram.toBuilder().setEntries(
                        entryIndex,
                        existingEntry.toBuilder().addCandidates(
                            bigramCandidate {
                                text = candidateText
                                frequency = incrementWeight
                            },
                        ).build(),
                    ).build()
                }
            }

            userBigram.toBuilder().addEntries(
                bigramEntry {
                    text = entryText
                }.toBuilder().addCandidates(
                    bigramCandidate {
                        text = candidateText
                        frequency = incrementWeight
                    },
                ).build(),
            ).build()
        }
    }
}
