package com.karrar.movieapp.domain.usecases.appPreferences

import com.karrar.movieapp.data.repository.AppPreferencesRepository
import javax.inject.Inject

class SetAppLanguageUseCase @Inject constructor(
    private val appPreferencesRepository: AppPreferencesRepository
) {
    suspend operator fun invoke(language : String) {
        appPreferencesRepository.setAppLanguage(language = language)
    }
}