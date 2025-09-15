package com.karrar.movieapp.domain.usecases.appConfigutations

import com.karrar.movieapp.data.repository.AppConfigurationsRepository
import javax.inject.Inject

class GetAppLanguageUseCase @Inject constructor(
    private val appConfigurationsRepository: AppConfigurationsRepository
) {
    operator fun invoke() = appConfigurationsRepository.getAppLanguage()
}