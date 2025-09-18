package com.karrar.movieapp.domain.usecases.appPreferences

import com.karrar.movieapp.data.repository.AppPreferencesRepository
import javax.inject.Inject

class GetFirstLaunchUseCase  @Inject constructor(
    private val appPreferencesRepository: AppPreferencesRepository
) {
    operator fun invoke() = appPreferencesRepository.isFirstLaunch()
}