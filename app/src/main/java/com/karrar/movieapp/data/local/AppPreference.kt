package com.karrar.movieapp.data.local

import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

interface AppPreferences{
    fun isFirstLaunch(): Flow<Boolean>
    suspend fun setFirstLaunch(isFirst: Boolean = false)
    fun isDarkTheme(): Flow<Boolean>
    suspend fun setDarkTheme(enabled: Boolean)
    fun getAppLanguage(): Flow<String>
    suspend fun setAppLanguage(languageCode: String)

    suspend fun getCategoryTipStatus(): Boolean

    suspend fun closeCategoryTip()
}


class AppPreferencesImpl @Inject constructor(
    private val dataStorePreferences: DataStorePreferences
) : AppPreferences {
    override fun isFirstLaunch(): Flow<Boolean> {
        return dataStorePreferences.readBooleanFlow(KEY_FIRST_LAUNCH, defaultValue = true)
    }

    override suspend fun setFirstLaunch(isFirst: Boolean) {
        dataStorePreferences.writeBoolean(KEY_FIRST_LAUNCH, isFirst)
    }

    override fun isDarkTheme(): Flow<Boolean> {
        return dataStorePreferences.readBooleanFlow(DARK_MODE_KEY, defaultValue = true)
    }

    override suspend fun setDarkTheme(enabled: Boolean) {
        dataStorePreferences.writeBoolean(DARK_MODE_KEY, enabled)
    }

    override fun getAppLanguage(): Flow<String> {
        return dataStorePreferences.readStringFlow(APP_LANGUAGE_KEY, defaultValue = "en")
    }

    override suspend fun setAppLanguage(languageCode: String) {
        dataStorePreferences.writeString(APP_LANGUAGE_KEY, languageCode)
    }

    override suspend fun getCategoryTipStatus(): Boolean {
        return dataStorePreferences.readBoolean(IS_CATEGORY_TIP_SHOWN) ?: true
    }

    override suspend fun closeCategoryTip() {
        dataStorePreferences.writeBoolean(IS_CATEGORY_TIP_SHOWN, false)
    }

    companion object DataStorePreferencesKeys {
        const val DARK_MODE_KEY = "dark_mode_enabled"
        const val APP_LANGUAGE_KEY = "app_language"
        const val KEY_FIRST_LAUNCH = "first_launch"
        const val IS_CATEGORY_TIP_SHOWN = "category_details_tip"

    }
}
