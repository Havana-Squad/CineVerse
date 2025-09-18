package com.karrar.movieapp.domain.usecases.appPreferences

import com.karrar.movieapp.data.repository.AppPreferencesRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow


class GetAppThemeUseCase @Inject constructor(private val appPreferencesRepository: AppPreferencesRepository) {
    operator fun invoke(): Flow<Boolean> {
        return appPreferencesRepository.isAppInDarkTheme()
    }
}