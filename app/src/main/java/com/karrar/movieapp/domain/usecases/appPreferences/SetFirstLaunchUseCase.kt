package com.karrar.movieapp.domain.usecases.appPreferences

import com.karrar.movieapp.data.repository.AppPreferencesRepository
import jakarta.inject.Inject

class SetFirstLaunchUseCase @Inject constructor(
    private val appPreferencesRepository: AppPreferencesRepository
){
    suspend operator fun invoke(){
        appPreferencesRepository.setFirstLaunchDone()
    }
}