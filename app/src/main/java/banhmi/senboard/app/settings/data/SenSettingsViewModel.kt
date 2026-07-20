package banhmi.senboard.app.settings.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SenSettingsViewModel(private val repository: SenSettingsRepository) : ViewModel() {
    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                require(modelClass.isAssignableFrom(SenSettingsViewModel::class.java)) {
                    "Unknown ViewModel class: ${modelClass.name}"
                }

                val application = checkNotNull(extras[APPLICATION_KEY])
                val repository = SenSettingsRepository(application)

                return SenSettingsViewModel(repository) as T
            }
        }
    }

    private fun <T> Flow<T>.asStateFlow(initialValue: T): StateFlow<T> = stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = initialValue,
    )

    private fun <T> updater(action: suspend (T) -> Unit): (T) -> Unit = { value ->
        viewModelScope.launch { action(value) }
    }

    val soundsAndHapticsState =
        repository.soundsAndHapticsFlow.asStateFlow(SoundsAndHapticsSettings())

    val aboutState = repository.aboutFlow.asStateFlow(AboutSettings())

    val updateHapticEnabled = updater(repository::updateHapticEnabled)

    val updateHapticIntensity = updater(repository::updateHapticIntensity)

    val updateSoundVolume = updater(repository::updateSoundVolume)

    val updateEasterEggEnabled = updater(repository::updateEasterEggEnabled)
}
