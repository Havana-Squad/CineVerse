package com.karrar.movieapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karrar.movieapp.domain.usecases.appPreferences.GetAppLanguageUseCase
import com.karrar.movieapp.domain.usecases.appPreferences.GetAppThemeUseCase
import com.karrar.movieapp.domain.usecases.appPreferences.GetFirstLaunchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getDarkModeUseCase: GetAppThemeUseCase,
    private val getAppLanguageUseCase: GetAppLanguageUseCase,
    private val getFirstLaunchUseCase: GetFirstLaunchUseCase,
) : ViewModel() {

    private val _darkMode = MutableStateFlow(true)
    val darkMode = _darkMode.asStateFlow()

    private val _language = MutableStateFlow("en")
    val language = _language.asStateFlow()

    private val _isFirstLaunch = MutableStateFlow(false)
    val isFirstLaunch = _isFirstLaunch.asStateFlow()

    init {
        viewModelScope.launch {
            getDarkModeUseCase().collect { dark ->
                _darkMode.value = dark
            }
        }

        viewModelScope.launch {
            getAppLanguageUseCase().collect { lang ->
                _language.value = lang
            }
        }

        viewModelScope.launch {
            getFirstLaunchUseCase().collect { firstLaunch ->
                _isFirstLaunch.value = firstLaunch
            }
        }
    }
}
