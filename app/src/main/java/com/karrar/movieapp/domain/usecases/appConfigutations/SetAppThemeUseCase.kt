package com.karrar.movieapp.domain.usecases.appConfigutations

import com.karrar.movieapp.data.repository.AppConfigurationsRepository
import jakarta.inject.Inject

class SetAppThemeUseCase @Inject constructor(
    private val appConfigurationsRepository: AppConfigurationsRepository
) {
    suspend operator fun invoke(isDarkTheme: Boolean) {
        appConfigurationsRepository.setDarkTheme(isDarkTheme)
    }
}