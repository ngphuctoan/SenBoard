package banhmi.senboard.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import banhmi.senboard.ime.engine.VietnameseEngine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SenPreferencesRepository @Inject constructor(private val dataStore: DataStore<Preferences>) {
    val preferencesFlow: Flow<SenPreferences> = dataStore.data
        .catch { exception ->
            when (exception) {
                // Tell the consumer that the settings are empty/default when failed to get the data store
                is IOException -> emit(emptyPreferences())
                else -> throw exception
            }
        }
        .map { preferences ->
            SenPreferences(
                vietnameseEngine = preferences[vietnameseEngine] ?: VietnameseEngine.Cvnss40.value,
                autoCapitalizationEnabled = preferences[autoCapitalizationEnabled] ?: true,
                spaceBarShortcutEnabled = preferences[spaceBarShortcutEnabled] ?: true,
                easterEggEnabled = preferences[easterEggEnabled] ?: false,
                showKeyBackground = preferences[showKeyBackground] ?: false,
                hapticsEnabled = preferences[hapticsEnabled] ?: true,
                hapticsIntensity = preferences[hapticsIntensity] ?: 50,
            )
        }

    // Helper function since there are a lot of settings @~@
    private suspend fun <T> updatePreferences(key: Preferences.Key<T>, value: T) {
        dataStore.edit { preferences -> preferences[key] = value }
    }

    // Pass in the enum instead, inside we do the value access for convenience
    suspend fun updateVietnameseEngine(newVietnameseEngine: VietnameseEngine) = updatePreferences(
        vietnameseEngine, newVietnameseEngine.value,
    )

    suspend fun updateAutoCapitalizationEnabled(newAutoCapitalizationEnabled: Boolean) = updatePreferences(
        autoCapitalizationEnabled, newAutoCapitalizationEnabled,
    )

    suspend fun updateSpaceBarShortcutEnabled(newSpaceBarShortcutEnabled: Boolean) = updatePreferences(
        spaceBarShortcutEnabled, newSpaceBarShortcutEnabled,
    )

    suspend fun updateEasterEggEnabled(newEasterEggEnabled: Boolean) = updatePreferences(
        easterEggEnabled, newEasterEggEnabled,
    )

    suspend fun updateShowKeyBackground(newShowKeyBackground: Boolean) = updatePreferences(
        showKeyBackground, newShowKeyBackground,
    )

    suspend fun updateHapticsEnabled(newHapticsEnabled: Boolean) = updatePreferences(
        hapticsEnabled, newHapticsEnabled,
    )

    suspend fun updateHapticsIntensity(newHapticsIntensity: Int) = updatePreferences(
        hapticsIntensity, newHapticsIntensity,
    )
}
