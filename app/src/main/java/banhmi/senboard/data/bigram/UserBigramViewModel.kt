package banhmi.senboard.data.bigram

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import banhmi.senboard.model.BigramDataset
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

object UserBigramViewModelDefaults {
    @JvmStatic
    internal val IncrementWeight = 0.1f
}

@HiltViewModel
class UserBigramViewModel @Inject constructor(
    private val repository: UserBigramRepository,
) : ViewModel() {
    // Manual construction using Factory for classes not supported by Hilt
    companion object {
        val REPOSITORY_KEY = CreationExtras.Key<UserBigramRepository>()

        val Factory = viewModelFactory {
            initializer {
                // Repository is required to be passed to the factory
                val repository = requireNotNull(this[REPOSITORY_KEY])
                UserBigramViewModel(repository)
            }
        }
    }

    val bigramDataset = repository.userBigramFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5.seconds),
        initialValue = BigramDataset(entries = emptyList()),
    )

    fun saveBigram(
        entryText: String,
        candidateText: String,
        incrementWeight: Float = UserBigramViewModelDefaults.IncrementWeight,
    ) {
        /* Disable saving user input as bigram for the time being.
        I think that saving user key taps is a very terrible idea */
        return

        @Suppress("UNREACHABLE_CODE")
        viewModelScope.launch {
            repository.saveBigram(entryText, candidateText, incrementWeight)
        }
    }
}
