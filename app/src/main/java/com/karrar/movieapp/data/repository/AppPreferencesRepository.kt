package com.karrar.movieapp.data.repository

import kotlinx.coroutines.flow.Flow

interface AppPreferencesRepository {
    fun isAppInDarkTheme(): Flow<Boolean>
    suspend fun setDarkTheme(enabled: Boolean)

    fun getAppLanguage(): Flow<String>
    suspend fun setAppLanguage(language: String)

    fun isFirstLaunch(): Flow<Boolean>
    suspend fun setFirstLaunchDone()
}