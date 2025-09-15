package com.karrar.movieapp.ui.main


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karrar.movieapp.domain.usecases.appConfigutations.GetAppLanguageUseCase
import com.karrar.movieapp.domain.usecases.appConfigutations.GetAppThemeUseCase
import com.karrar.movieapp.domain.usecases.appConfigutations.SetAppLanguageUseCase
import com.karrar.movieapp.domain.usecases.appConfigutations.SetAppThemeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getDarkModeUseCase: GetAppThemeUseCase,
    private val getAppLanguageUseCase: GetAppLanguageUseCase
) : ViewModel() {

    private val _darkMode = MutableStateFlow(false)
    val darkMode = _darkMode.asStateFlow()

    private val _language = MutableStateFlow("en")
    val language = _language.asStateFlow()

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
    }
}
