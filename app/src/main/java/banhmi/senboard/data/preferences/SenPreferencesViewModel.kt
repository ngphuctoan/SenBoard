package banhmi.senboard.data.preferences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import banhmi.senboard.ime.engine.VietnameseEngine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SenPreferencesViewModel @Inject constructor(private val repository: SenPreferencesRepository) : ViewModel() {
    val preferences: StateFlow<SenPreferences> = repository.preferencesFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SenPreferences(),
    )

    // I love boilerplate code :D
    private fun <T> updatePreferences(action: suspend (T) -> Unit, value: T) {
        viewModelScope.launch { action(value) }
    }

    // No need to worry about direct enum value access!
    fun updateVietnameseEngine(newVietnameseEngine: VietnameseEngine) = updatePreferences(
        repository::updateVietnameseEngine, newVietnameseEngine,
    )

    fun updateAutoCapitalizationEnabled(newAutoCapitalizationEnabled: Boolean) = updatePreferences(
        repository::updateAutoCapitalizationEnabled, newAutoCapitalizationEnabled,
    )

    fun updateSpaceBarShortcutEnabled(newSpaceBarShortcutEnabled: Boolean) = updatePreferences(
        repository::updateSpaceBarShortcutEnabled, newSpaceBarShortcutEnabled,
    )

    fun updateEasterEggEnabled(newEasterEggEnabled: Boolean) = updatePreferences(
        repository::updateEasterEggEnabled, newEasterEggEnabled,
    )

    fun updateShowKeyBackground(newShowKeyBackground: Boolean) = updatePreferences(
        repository::updateShowKeyBackground, newShowKeyBackground,
    )

    fun updateHapticsEnabled(newHapticsIntensity: Boolean) = updatePreferences(
        repository::updateHapticsEnabled, newHapticsIntensity,
    )

    fun updateHapticsIntensity(newHapticIntensity: Int) = updatePreferences(
        repository::updateHapticsIntensity, newHapticIntensity,
    )
}
