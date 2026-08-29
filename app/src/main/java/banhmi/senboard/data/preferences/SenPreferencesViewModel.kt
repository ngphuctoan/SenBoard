package banhmi.senboard.data.preferences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import banhmi.senboard.engine.VietnameseEngineType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class SenPreferencesViewModel @Inject constructor(
    private val repository: SenPreferencesRepository,
) : ViewModel() {
    // Manual construction using Factory for classes not supported by Hilt
    companion object {
        val REPOSITORY_KEY = CreationExtras.Key<SenPreferencesRepository>()

        val Factory = viewModelFactory {
            initializer {
                // Repository is required to be passed to the factory
                val repository = requireNotNull(this[REPOSITORY_KEY])
                SenPreferencesViewModel(repository)
            }
        }
    }

    val preferencesState = repository.preferencesFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5.seconds),
        /* Set the initial value as the first emitted value from the flow. This makes sure that
        the first value when collected is actually from the DataStore, skipping the initial values,
        at a cost of longer initial loading times */
        initialValue = runBlocking { repository.preferencesFlow.first() },
        // initialValue = SenPreferences(),
    )

    // I love boilerplate code :D
    private fun <T> updatePreferences(
        action: suspend (T) -> Unit,
        value: T,
    ) {
        viewModelScope.launch { action(value) }
    }

    // No need to worry about direct enum value access!
    fun updateVietnameseEngineType(
        newVietnameseEngineType: VietnameseEngineType,
    ) = updatePreferences(repository::updateVietnameseEngineType, newVietnameseEngineType)

    fun updateAutoCapitalizationEnabled(
        newAutoCapitalizationEnabled: Boolean,
    ) = updatePreferences(repository::updateAutoCapitalizationEnabled, newAutoCapitalizationEnabled)

    fun updateSpaceBarShortcutEnabled(
        newSpaceBarShortcutEnabled: Boolean,
    ) = updatePreferences(repository::updateSpaceBarShortcutEnabled, newSpaceBarShortcutEnabled)

    fun updateWordSuggestionsEnabled(
        newWordSuggestionsEnabled: Boolean,
    ) = updatePreferences(repository::updateWordSuggestionsEnabled, newWordSuggestionsEnabled)

    fun updateNumberRowEnabled(
        newNumberRowEnabled: Boolean,
    ) = updatePreferences(repository::updateNumberRowEnabled, newNumberRowEnabled)

    fun updateKeyBackgroundEnabled(
        newKeyBackgroundEnabled: Boolean,
    ) = updatePreferences(repository::updateKeyBackgroundEnabled, newKeyBackgroundEnabled)

    fun updateKeyBackgroundShadowEnabled(
        newKeyBackgroundShadowEnabled: Boolean,
    ) =
        updatePreferences(repository::updateKeyBackgroundShadowEnabled, newKeyBackgroundShadowEnabled)

    fun updateHapticsEnabled(
        newHapticsIntensity: Boolean,
    ) = updatePreferences(repository::updateHapticsEnabled, newHapticsIntensity)

    fun updateHapticsIntensity(
        newHapticIntensity: Int,
    ) = updatePreferences(repository::updateHapticsIntensity, newHapticIntensity)

    fun updateEasterEggsEnabled(
        newEasterEggsEnabled: Boolean,
    ) = updatePreferences(repository::updateEasterEggsEnabled, newEasterEggsEnabled)

    fun updateAaaaaModeEnabled(
        newAaaaaModeEnabled: Boolean,
    ) = updatePreferences(repository::updateAaaaaModeEnabled, newAaaaaModeEnabled)

    fun updateDeveloperOptionsEnabled(
        newDeveloperOptionsEnabled: Boolean,
    ) = updatePreferences(repository::updateDeveloperOptionsEnabled, newDeveloperOptionsEnabled)
}
