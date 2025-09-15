package com.karrar.movieapp.domain.usecases.appConfigutations

import com.karrar.movieapp.data.repository.AppConfigurationsRepository
import jakarta.inject.Inject

class SetFirstLaunchUseCase @Inject constructor(
    private val appConfigurationsRepository: AppConfigurationsRepository
){
    suspend operator fun invoke(){
        appConfigurationsRepository.setFirstLaunchDone()
    }
}