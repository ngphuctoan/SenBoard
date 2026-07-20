package banhmi.senboard.app.settings.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.io.IOException

class SenSettingsRepository(private val context: Context) {
    private fun <T> mapPreferencesToFlow(mapper: (Preferences) -> T): Flow<T> =
        context.dataStore.data.catch {
            when (it) {
                is IOException -> emit(emptyPreferences())
                else -> throw it
            }
        }.map(mapper).distinctUntilChanged()

    private suspend fun <T> updateSetting(key: Preferences.Key<T>, value: T) =
        context.dataStore.edit { it[key] = value }

    val soundsAndHapticsFlow: Flow<SoundsAndHapticsSettings> = mapPreferencesToFlow { preferences ->
        SoundsAndHapticsSettings(
            hapticEnabled = preferences[SenSettingsKeys.hapticEnabled] ?: true,
            hapticIntensity = preferences[SenSettingsKeys.hapticIntensity] ?: 60,
            soundVolume = preferences[SenSettingsKeys.soundVolume] ?: 50,
        )
    }

    val aboutFlow: Flow<AboutSettings> = mapPreferencesToFlow { preferences ->
        AboutSettings(
            easterEggEnabled = preferences[SenSettingsKeys.easterEggEnabled] ?: false,
        )
    }

    suspend fun updateHapticEnabled(enabled: Boolean) =
        updateSetting(SenSettingsKeys.hapticEnabled, enabled)

    suspend fun updateHapticIntensity(intensity: Int) =
        updateSetting(SenSettingsKeys.hapticIntensity, intensity.coerceIn(0, 100))

    suspend fun updateSoundVolume(volume: Int) =
        updateSetting(SenSettingsKeys.soundVolume, volume.coerceIn(0, 100))

    suspend fun updateEasterEggEnabled(enabled: Boolean) =
        updateSetting(SenSettingsKeys.easterEggEnabled, enabled)
}
