package com.karrar.movieapp.ui.search

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.map
import com.karrar.movieapp.domain.usecases.searchUseCase.*
import com.karrar.movieapp.ui.allMedia.Error
import com.karrar.movieapp.ui.base.BaseViewModel
import com.karrar.movieapp.ui.search.adapters.ActorSearchInteractionListener
import com.karrar.movieapp.ui.search.adapters.MediaSearchInteractionListener
import com.karrar.movieapp.ui.search.adapters.SearchHistoryInteractionListener
import com.karrar.movieapp.ui.search.adapters.SearchSuggestionsInteractionListener
import com.karrar.movieapp.ui.search.mediaSearchUIState.MediaSearchUIState
import com.karrar.movieapp.ui.search.mediaSearchUIState.MediaTypes
import com.karrar.movieapp.ui.search.mediaSearchUIState.MediaUIState
import com.karrar.movieapp.ui.search.mediaSearchUIState.SearchHistoryUIState
import com.karrar.movieapp.ui.search.mediaSearchUIState.SearchSuggestionUiState
import com.karrar.movieapp.ui.search.uiStatMapper.SearchHistoryUIStateMapper
import com.karrar.movieapp.ui.search.uiStatMapper.SearchMediaUIStateMapper
import com.karrar.movieapp.ui.search.uiStatMapper.SearchSuggestionUiStateMapper
import com.karrar.movieapp.utilities.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchHistoryUIStateMapper: SearchHistoryUIStateMapper,
    private val searchMediaUIStateMapper: SearchMediaUIStateMapper,
    private val searchSuggestionUiStateMapper: SearchSuggestionUiStateMapper,
    private val getSearchSuggestionsUseCase: GetSearchSuggestionsUseCase,
    private val getSearchForMovieUseCase: GetSearchForMovieUseCase,
    private val getSearchForSeriesUserCase: GetSearchForSeriesUserCase,
    private val getSearchForActorUseCase: GetSearchForActorUseCase,
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase,
    private val addSearchHistoryItemUseCase: AddSearchHistoryItemUseCase,
    private val deleteSearchHistoryItemUseCase: DeleteSearchHistoryItemUseCase,
    private val deleteAllSearchHistoryUseCase: DeleteAllSearchHistoryUseCase
) : BaseViewModel(), MediaSearchInteractionListener, ActorSearchInteractionListener,
    SearchHistoryInteractionListener, SearchSuggestionsInteractionListener {

    private val _uiState = MutableStateFlow(MediaSearchUIState())
    val uiState = _uiState.asStateFlow()

    private val _searchUIEvent = MutableStateFlow<Event<SearchUIEvent?>>(Event(null))
    val searchUIEvent = _searchUIEvent.asStateFlow()

    init {
        getAllSearchHistory()
    }

    override fun getData() {
        _searchUIEvent.update { Event(SearchUIEvent.ClickRetryEvent) }
    }

    private fun getAllSearchHistory() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                getSearchHistoryUseCase().collect { list ->
                    _uiState.update {
                        it.copy(searchHistory = list.map { item ->
                            searchHistoryUIStateMapper.map(
                                item
                            )
                        }, isLoading = false, isEmpty = false)
                    }
                }
            } catch (e: Throwable) {
                _uiState.update {
                    it.copy(error = listOf(Error(0, e.message.toString())))
                }
            }
        }
    }

    fun onSearchInputChange(searchTerm: CharSequence) {
        if(searchTerm.toString() != uiState.value.searchInput) {
            _uiState.update {
                it.copy(
                    searchInput = searchTerm.toString(),
                    isLoading = true,
                    isSearchResultVisible = false
                )
            }
        }
    }

    fun onMicrophoneClick() {
        _searchUIEvent.update { Event(SearchUIEvent.ClickMicrophoneEvent) }
    }

    fun onLoadSearchResults() {
        _uiState.update { it.copy(isSearchResultVisible = true) }
        viewModelScope.launch {
            when (_uiState.value.searchTypes) {
                MediaTypes.MOVIE -> onSearchForMovie()
                MediaTypes.TVS_SHOW -> onSearchForSeries()
                MediaTypes.ACTOR -> onSearchForActor()
            }
        }
    }


    fun onSearchForMovie() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = getSearchForMovieUseCase(uiState.value.searchInput).map { pagingData ->
                pagingData.map { item -> searchMediaUIStateMapper.map(item) }
            }
            _uiState.update {
                it.copy(
                    searchTypes = MediaTypes.MOVIE,
                    isLoading = false,
                    searchResult = result
                )
            }
        }
    }

    fun onSearchForSeries() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = getSearchForSeriesUserCase(uiState.value.searchInput).map { pagingData ->
                pagingData.map { item -> searchMediaUIStateMapper.map(item) }
            }
            _uiState.update {
                it.copy(
                    searchTypes = MediaTypes.TVS_SHOW,
                    isLoading = false,
                    searchResult = result
                )
            }
        }
    }

    fun onSearchForActor() {
        viewModelScope.launch (Dispatchers.IO){
            val result = getSearchForActorUseCase(uiState.value.searchInput).map { pagingData ->
                pagingData.map { item -> searchMediaUIStateMapper.map(item) }
            }
            _uiState.update {
                it.copy(
                    searchTypes = MediaTypes.ACTOR,
                    isLoading = false,
                    searchResult = result
                )
            }
        }
    }

    fun getSearchSuggestions() {
        viewModelScope.launch (Dispatchers.IO){
            _uiState.update {
                it.copy(
                    searchSuggestions = getSearchSuggestionsUseCase
                        .invoke(uiState.value.searchInput)
                        .map(searchSuggestionUiStateMapper::map)
                )
            }
        }
    }

    fun addSearchHistoryItem() {
        viewModelScope.launch {
            if (_uiState.value.searchInput.isNotBlank()) {
                addSearchHistoryItemUseCase(_uiState.value.searchInput)
            }
        }
    }

    fun onClearAllSearchHistory() {
        viewModelScope.launch {
            deleteAllSearchHistoryUseCase()
        }
    }




    override fun onClickMediaResult(media: MediaUIState) {
        _searchUIEvent.update { Event(SearchUIEvent.ClickMediaEvent(media)) }
    }

    override fun onClickActorResult(personID: Int, name: String) {
        _searchUIEvent.update { Event(SearchUIEvent.ClickActorEvent(personID)) }
    }

    override fun onClickSearchHistory(name: String) {
        onSearchInputChange(name)
    }

    override fun onClickDeleteSearchHistoryItem(searchHistoryItem: SearchHistoryUIState) {
        viewModelScope.launch(Dispatchers.IO){
            deleteSearchHistoryItemUseCase(searchHistoryItem.id)
        }
    }

    fun onClickBack() {
        _searchUIEvent.update { Event(SearchUIEvent.ClickBackEvent) }
    }

    fun setErrorUiState(combinedLoadStates: CombinedLoadStates, itemCount: Int) {
        when (combinedLoadStates.refresh) {
            is LoadState.Loading -> {
                _uiState.update {
                    it.copy(isLoading = true, error = emptyList(), isEmpty = false)
                }
            }
            is LoadState.Error -> {
                _uiState.update {
                    it.copy(isLoading = false, error = listOf(Error(404, "")), isEmpty = false)
                }
            }
            is LoadState.NotLoading -> {
                if (itemCount < 1) {
                    _uiState.update {
                        it.copy(
                            isEmpty = true,
                            isLoading = false,
                            error = emptyList()
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isEmpty = false,
                            isLoading = false,
                            error = emptyList()
                        )
                    }
                }
            }
        }
    }

    fun onClickMediaType(mediaType: Int) {
        _uiState.update { it.copy(isLoading = true) }
        when(mediaType) {
            1 -> onSearchForMovie()
            2 -> onSearchForSeries()
            3 -> onSearchForActor()
        }
    }

    fun resetSearchSuggestionFlag() {
        _uiState.update{ it.copy(isSearchSuggestionClicked = false)}
    }

    override fun onClickSearchSuggestion(name: String) {
        Log.d("SearchSuggestionBug", "onClickSearchSuggestion: $name")
        _uiState.update{ it.copy(isSearchSuggestionClicked = true)}
        onSearchInputChange(name)
        onLoadSearchResults()
    }

}