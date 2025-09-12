package com.karrar.movieapp.ui.explore

import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.map
import com.karrar.movieapp.domain.usecases.GetGenreListUseCase
import com.karrar.movieapp.domain.usecases.GetMediaByGenreIDUseCase
import com.karrar.movieapp.ui.base.BaseViewModel
import com.karrar.movieapp.ui.explore.exploreUIState.ExploreUIState
import com.karrar.movieapp.ui.explore.exploreUIState.ExploringUIEvent
import com.karrar.movieapp.ui.explore.exploreUIState.GenreUIStateMapper
import com.karrar.movieapp.ui.explore.exploreUIState.MediaUIState
import com.karrar.movieapp.ui.explore.exploreUIState.MediaUIStateMapper
import com.karrar.movieapp.utilities.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.karrar.movieapp.ui.explore.exploreUIState.ErrorUIState
import com.karrar.movieapp.ui.explore.exploreUIState.ExploreViewMode
import com.karrar.movieapp.utilities.Constants
import javax.inject.Inject


@HiltViewModel
class ExploringViewModel @Inject constructor(
    private val getMediaByGenreUseCase: GetMediaByGenreIDUseCase,
    private val mediaUIStateMapper: MediaUIStateMapper,
    private val genreUIStateMapper: GenreUIStateMapper,
    private val getGenresUseCase: GetGenreListUseCase,
) : BaseViewModel(), ExploreInteractionListener {

    private val _uiState = MutableStateFlow(ExploreUIState())
    val uiState: StateFlow<ExploreUIState> = _uiState

    private val _exploringUIEvent: MutableStateFlow<Event<ExploringUIEvent>?> = MutableStateFlow(null)
    val exploringUIEvent= _exploringUIEvent.asStateFlow()

    init {
        getData()
    }

    override fun getData() {
        _uiState.update { it.copy(isLoading = true, error = emptyList()) }
        getMediaList()
        getGenres()
        _exploringUIEvent.update { Event(ExploringUIEvent.RetryEvent) }
    }

    fun getMediaList(
        selectedCategory: Int = uiState.value.selectedCategoryID,
        selectedMediaType: Int = uiState.value.selectedMediaTypeID
    ) {
        viewModelScope.launch {
            val result = getMediaByGenreUseCase(selectedMediaType, selectedCategory)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    media = result.map { pagingData -> pagingData.map { mediaUIStateMapper.map(it) } },
                    error = emptyList()
                )
            }
        }
    }

    private fun getGenres() {
        viewModelScope.launch {
            try {
                val genres = getGenresUseCase(uiState.value.selectedMediaTypeID).map { genreUIStateMapper.map(it) }
                _uiState.update {
                    it.copy(
                        genres = genres,
                        selectedCategoryID = Constants.FIRST_CATEGORY_ID
                    )
                }
            } catch (t: Throwable) {
                _uiState.update { it.copy(error = listOf(ErrorUIState(404, "")
                )) }
            }
        }
    }

    override fun onClickMedia(mediaItem: MediaUIState) {
        _exploringUIEvent.update { Event(ExploringUIEvent.ClickMediaEvent(mediaItem)) }
    }

    override fun onClickCategory(categoryId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(selectedCategoryID = categoryId, isLoading = true) }
            _exploringUIEvent.emit(Event(ExploringUIEvent.SelectedCategory(categoryId)))
        }
    }

    fun onClickSearch() {
        _exploringUIEvent.update { Event(ExploringUIEvent.SearchEvent) }
    }

    override fun onClickMediaType(mediaTypeId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(selectedMediaTypeID = mediaTypeId) }
            getGenres()
            _exploringUIEvent.emit(Event(ExploringUIEvent.SelectedMediaType(mediaTypeId)))
        }
    }

    override fun onClickViewMode(viewMode: ExploreViewMode) {
        viewModelScope.launch {
            if (uiState.value.selectedViewMode != viewMode) {
                _uiState.update { it.copy(selectedViewMode = viewMode) }
                _exploringUIEvent.emit(Event(ExploringUIEvent.SelectedViewMode(viewMode)))
            }
        }
    }

    fun setErrorUiState(combinedLoadStates: CombinedLoadStates) {
        when (combinedLoadStates.refresh) {
            is LoadState.NotLoading -> {
                _uiState.update {
                    it.copy(isLoading = false, error = emptyList())
                }
            }
            LoadState.Loading -> {
                _uiState.update {
                    it.copy(isLoading = false, error = emptyList())
                }
            }
            is LoadState.Error -> {
                _uiState.update {
                    it.copy(isLoading = false, error = listOf(ErrorUIState(404, "Error")))
                }
            }
        }
    }

}