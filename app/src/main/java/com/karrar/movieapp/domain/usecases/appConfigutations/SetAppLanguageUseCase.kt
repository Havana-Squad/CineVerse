package com.karrar.movieapp.domain.usecases.appConfigutations

import com.karrar.movieapp.data.repository.AppConfigurationsRepository
import javax.inject.Inject

class SetAppLanguageUseCase @Inject constructor(
    private val appConfigurationsRepository: AppConfigurationsRepository
) {
    suspend operator fun invoke(language : String) {
        appConfigurationsRepository.setAppLanguage(language = language)
    }
}