package com.karrar.movieapp.ui.explore.exploreUIState

import com.karrar.movieapp.ui.category.uiState.CategoryUIEvent
import com.karrar.movieapp.utilities.Constants

sealed interface ExploringUIEvent {
    object RetryEvent : ExploringUIEvent
    object SearchEvent : ExploringUIEvent

    data class ClickMediaEvent(val mediaItem: MediaUIState): ExploringUIEvent
    data class SelectedCategory(val categoryID: Int = Constants.FIRST_CATEGORY_ID): ExploringUIEvent
    data class SelectedMediaType(val mediaTypeID: Int = Constants.MOVIE_CATEGORIES_ID): ExploringUIEvent
    data class SelectedViewMode(val viewMode: ExploreViewMode): ExploringUIEvent
}