package com.karrar.movieapp.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karrar.movieapp.R
import com.karrar.movieapp.domain.usecases.appPreferences.SetFirstLaunchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val setFirstLaunchUseCase: SetFirstLaunchUseCase,
) : ViewModel() {

    val imagePages = listOf(
        R.drawable.on_boarding_1st_image,
        R.drawable.on_boarding_2nd_image,
        R.drawable.on_boarding_3rd_image
    )

    val textPages = listOf(
        OnboardingText(R.string.onboarding_title_1, R.string.onboarding_desc_1),
        OnboardingText(R.string.onboarding_title_2, R.string.onboarding_desc_2),
        OnboardingText(R.string.onboarding_title_3, R.string.onboarding_desc_3),
    )


    fun completeOnboarding() {
        viewModelScope.launch {
            setFirstLaunchUseCase()
        }
    }
}

