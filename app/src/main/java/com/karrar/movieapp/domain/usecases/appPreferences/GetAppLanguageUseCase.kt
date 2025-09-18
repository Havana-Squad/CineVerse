package com.karrar.movieapp.domain.usecases.appPreferences

import com.karrar.movieapp.data.repository.AppPreferencesRepository
import javax.inject.Inject

class GetAppLanguageUseCase @Inject constructor(
    private val appPreferencesRepository: AppPreferencesRepository
) {
    operator fun invoke() = appPreferencesRepository.getAppLanguage()
}