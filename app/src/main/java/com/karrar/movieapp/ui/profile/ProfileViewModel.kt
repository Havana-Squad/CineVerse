package com.karrar.movieapp.ui.profile

import androidx.lifecycle.viewModelScope
import com.karrar.movieapp.domain.usecases.CheckIfLoggedInUseCase
import com.karrar.movieapp.domain.usecases.GetAccountDetailsUseCase
import com.karrar.movieapp.domain.usecases.LogoutUseCase
import com.karrar.movieapp.domain.usecases.appConfigutations.GetAppLanguageUseCase
import com.karrar.movieapp.domain.usecases.appConfigutations.GetAppThemeUseCase
import com.karrar.movieapp.domain.usecases.appConfigutations.SetAppLanguageUseCase
import com.karrar.movieapp.domain.usecases.appConfigutations.SetAppThemeUseCase
import com.karrar.movieapp.ui.base.BaseViewModel
import com.karrar.movieapp.utilities.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getAccountDetailsUseCase: GetAccountDetailsUseCase,
    private val accountUIStateMapper: AccountUIStateMapper,
    private val checkIfLoggedInUseCase: CheckIfLoggedInUseCase,
    private val getAppThemeUseCase: GetAppThemeUseCase,
    private val setAppThemeUseCase: SetAppThemeUseCase,
    private val getAppLanguageUseCase: GetAppLanguageUseCase,
    private val setAppLanguageUseCase: SetAppLanguageUseCase,
    private val logoutUseCase: LogoutUseCase
) : BaseViewModel() {

    private val _profileDetailsUIState = MutableStateFlow(ProfileUIState())
    val profileDetailsUIState = _profileDetailsUIState.asStateFlow()

    private val _profileUIEvent: MutableStateFlow<Event<ProfileUIEvent?>> =
        MutableStateFlow(Event(null))
    val profileUIEvent = _profileUIEvent.asStateFlow()

    private val _darkMode = MutableStateFlow(true)
    val darkMode = _darkMode.asStateFlow()

    private val _language = MutableStateFlow("en")
    val language = _language.asStateFlow()

    private val _logoutEvent = MutableSharedFlow<Unit>()
    val logoutEvent = _logoutEvent.asSharedFlow()

    init {
        loadTheme()
        loadLanguage()
        getData()
        checkIfUserLoggedIn()
    }

    override fun getData() {
        getProfileDetails()
    }

    fun logout() {
        viewModelScope.launch {
            try {
                logoutUseCase()
                _profileUIEvent.emit(Event(ProfileUIEvent.LoginEvent))

            } catch (t: Throwable) {
                _profileDetailsUIState.update {
                    it.copy(isLoading = false, error = true)
                }
            }
        }
    }

    private fun checkIfUserLoggedIn() {
        if (checkIfLoggedInUseCase()) {
            _profileDetailsUIState.update {
                it.copy(isLoggedIn = true)
            }
        }
    }

    private fun loadTheme() {
        viewModelScope.launch {
            getAppThemeUseCase()
                .distinctUntilChanged()
                .collect { value ->
                    _darkMode.value = value
                }
        }
    }

    fun toggleThemeSwitch(isEnabled: Boolean) {
        viewModelScope.launch {
            setAppThemeUseCase(isDarkTheme = isEnabled)
        }
    }

    private fun loadLanguage() {
        viewModelScope.launch {
            getAppLanguageUseCase().collect { lang ->
                _language.value = lang
            }
        }
    }

    fun setLanguage(language: String) {
        viewModelScope.launch {
            setAppLanguageUseCase(language)
            _language.value = language
        }
    }

    private fun getProfileDetails() {
        if (checkIfLoggedInUseCase()) {
            _profileDetailsUIState.update {
                it.copy(isLoading = true, isLoggedIn = true, error = false)
            }

            viewModelScope.launch {
                try {
                    val accountDetails = accountUIStateMapper.map(getAccountDetailsUseCase())
                    _profileDetailsUIState.update {
                        it.copy(
                            avatarPath = accountDetails.avatarPath,
                            name = accountDetails.name,
                            username = accountDetails.username,
                            isLoading = false
                        )
                    }
                } catch (t: Throwable) {
                    _profileDetailsUIState.update {
                        it.copy(isLoading = false, error = true)
                    }
                }
            }
        } else {
            _profileDetailsUIState.update {
                it.copy(isLoggedIn = false)
            }
        }
    }

    fun goToWebsite() {
        _profileUIEvent.update { Event(ProfileUIEvent.OpenEditProfileWebsite) }
    }


    fun onClickRatedMovies() {
        _profileUIEvent.update { Event(ProfileUIEvent.RatedMoviesEvent) }
    }


    fun onClickWatchHistory() {
        _profileUIEvent.update { Event(ProfileUIEvent.WatchHistoryEvent) }
    }

    fun onClickMyCollection() {
        _profileUIEvent.update { Event(ProfileUIEvent.MyCollectionEvent) }
    }
}