package com.karrar.movieapp.domain.usecases.appConfigutations

import com.karrar.movieapp.data.repository.AppConfigurationsRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow


class GetAppThemeUseCase @Inject constructor(private val appConfigurationsRepository: AppConfigurationsRepository) {
    operator fun invoke(): Flow<Boolean> {
        return appConfigurationsRepository.isAppInDarkTheme()
    }
}