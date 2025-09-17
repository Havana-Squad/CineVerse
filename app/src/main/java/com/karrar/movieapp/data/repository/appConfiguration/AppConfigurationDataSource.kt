package com.karrar.movieapp.data.repository.appConfiguration

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppConfigurationsDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
)  {
    val isFirstLaunch: Flow<Boolean> = dataStore.data
        .map { preferences -> preferences[KEY_FIRST_LAUNCH] ?: true }

    suspend fun setFirstLaunch() {
        dataStore.edit { preferences ->
            preferences[KEY_FIRST_LAUNCH] = false
        }
    }

    val isDarkTheme: Flow<Boolean> = dataStore.data
        .map { preferences -> preferences[DARK_MODE_KEY] ?: true }

    suspend fun setDarkTheme(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[DARK_MODE_KEY] = enabled
        }
    }

    val appLanguage: Flow<String> = dataStore.data
        .map { preferences -> preferences[APP_LANGUAGE_KEY] ?: "en" }

    suspend fun setAppLanguage(languageCode: String) {
        dataStore.edit { preferences ->
            preferences[APP_LANGUAGE_KEY] = languageCode
        }
    }

    companion object {
        private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode_enabled")
        private val APP_LANGUAGE_KEY = stringPreferencesKey("app_language")
        private val KEY_FIRST_LAUNCH = booleanPreferencesKey("first_launch")
    }
}
