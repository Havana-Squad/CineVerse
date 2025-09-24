package com.karrar.movieapp.ui.search.uiStatMapper

import com.karrar.movieapp.domain.mappers.Mapper
import com.karrar.movieapp.domain.models.Media
import com.karrar.movieapp.domain.models.SearchHistory
import com.karrar.movieapp.ui.search.mediaSearchUIState.SearchHistoryUIState
import com.karrar.movieapp.ui.search.mediaSearchUIState.SearchSuggestionUiState
import javax.inject.Inject

class SearchSuggestionUiStateMapper @Inject constructor(): Mapper<Media, SearchSuggestionUiState> {
    override fun map(input: Media): SearchSuggestionUiState {
        return SearchSuggestionUiState(
            input.mediaName
        )
    }
}