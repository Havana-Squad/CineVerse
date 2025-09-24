package com.karrar.movieapp.data.repository

import com.karrar.movieapp.data.local.AppPreferences
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class AppPreferencesRepositoryImpl @Inject constructor(
    private val dataSource: AppPreferences
): BaseRepository() ,AppPreferencesRepository {

    override fun isAppInDarkTheme(): Flow<Boolean> {
        return dataSource.isDarkTheme()
    }

    override suspend fun setDarkTheme(enabled: Boolean) {
        dataSource.setDarkTheme(enabled)
    }

    override fun getAppLanguage(): Flow<String> {
        return dataSource.getAppLanguage()
    }

    override suspend fun setAppLanguage(language: String) {
        dataSource.setAppLanguage(language)
    }

    override fun isFirstLaunch(): Flow<Boolean> {
        return dataSource.isFirstLaunch()
    }

    override suspend fun setFirstLaunchDone() {
        dataSource.setFirstLaunch()
    }
}