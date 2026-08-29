package banhmi.senboard.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import banhmi.senboard.engine.VietnameseEngineType
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SenPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    val preferencesFlow = dataStore.data.catch { exception ->
        when (exception) {
            // Tell the consumer that the settings are empty/default when failed to get the data store
            is IOException -> emit(emptyPreferences())
            else -> throw exception
        }
    }.map { preferences ->
        val engineType = VietnameseEngineType.entries.firstOrNull { type ->
            type.id == preferences[vietnameseEngineType]
        }

        SenPreferences(
            vietnameseEngineType = engineType ?: VietnameseEngineType.Cvss,
            autoCapitalizationEnabled = preferences[autoCapitalizationEnabled] ?: true,
            spaceBarShortcutEnabled = preferences[spaceBarShortcutEnabled] ?: true,
            wordSuggestionsEnabled = preferences[wordSuggestionsEnabled] ?: true,
            numberRowEnabled = preferences[numberRowEnabled] ?: false,
            keyBackgroundEnabled = preferences[keyBackgroundEnabled] ?: true,
            keyBackgroundShadowEnabled = preferences[keyBackgroundShadowEnabled] ?: true,
            hapticsEnabled = preferences[hapticsEnabled] ?: true,
            hapticsIntensity = preferences[hapticsIntensity] ?: 66,
            easterEggsEnabled = preferences[easterEggsEnabled] ?: false,
            aaaaaModeEnabled = preferences[aaaaaModeEnabled] ?: false,
            developerOptionsEnabled = preferences[developerOptionsEnabled] ?: false,
        )
    }

    // Helper function since there are a lot of settings @~@
    private suspend fun <T> updatePreferences(
        key: Preferences.Key<T>,
        value: T,
    ) {
        dataStore.edit { preferences -> preferences[key] = value }
    }

    // Pass in the enum instead, inside we do the value access for convenience
    suspend fun updateVietnameseEngineType(
        newVietnameseEngineType: VietnameseEngineType,
    ) = updatePreferences(vietnameseEngineType, newVietnameseEngineType.id)

    suspend fun updateAutoCapitalizationEnabled(
        newAutoCapitalizationEnabled: Boolean,
    ) = updatePreferences(autoCapitalizationEnabled, newAutoCapitalizationEnabled)

    suspend fun updateSpaceBarShortcutEnabled(
        newSpaceBarShortcutEnabled: Boolean,
    ) = updatePreferences(spaceBarShortcutEnabled, newSpaceBarShortcutEnabled)

    suspend fun updateWordSuggestionsEnabled(
        newWordSuggestionsEnabled: Boolean,
    ) = updatePreferences(wordSuggestionsEnabled, newWordSuggestionsEnabled)

    suspend fun updateNumberRowEnabled(
        newNumberRowEnabled: Boolean,
    ) = updatePreferences(numberRowEnabled, newNumberRowEnabled)

    suspend fun updateKeyBackgroundEnabled(
        newKeyBackgroundEnabled: Boolean,
    ) = updatePreferences(keyBackgroundEnabled, newKeyBackgroundEnabled)

    suspend fun updateKeyBackgroundShadowEnabled(
        newKeyBackgroundShadowEnabled: Boolean,
    ) = updatePreferences(keyBackgroundShadowEnabled, newKeyBackgroundShadowEnabled)

    suspend fun updateHapticsEnabled(
        newHapticsEnabled: Boolean,
    ) = updatePreferences(hapticsEnabled, newHapticsEnabled)

    suspend fun updateHapticsIntensity(
        newHapticsIntensity: Int,
    ) = updatePreferences(hapticsIntensity, newHapticsIntensity)

    suspend fun updateEasterEggsEnabled(
        newEasterEggsEnabled: Boolean,
    ) = updatePreferences(easterEggsEnabled, newEasterEggsEnabled)

    suspend fun updateAaaaaModeEnabled(
        newAaaaaModeEnabled: Boolean,
    ) = updatePreferences(aaaaaModeEnabled, newAaaaaModeEnabled)

    suspend fun updateDeveloperOptionsEnabled(
        newDeveloperOptionsEnabled: Boolean,
    ) = updatePreferences(developerOptionsEnabled, newDeveloperOptionsEnabled)
}
