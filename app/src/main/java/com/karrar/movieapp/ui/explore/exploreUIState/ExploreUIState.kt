package com.karrar.movieapp.ui.explore.exploreUIState

import androidx.paging.PagingData
import com.karrar.movieapp.utilities.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow


data class ExploreUIState(
    val genres: List<GenreUIState> = emptyList(),
    val selectedCategoryID :Int = Constants.FIRST_CATEGORY_ID,
    val selectedMediaTypeID :Int = Constants.MOVIE_CATEGORIES_ID,
    val selectedViewMode: ExploreViewMode = ExploreViewMode.GRID,
    val media: Flow<PagingData<MediaUIState>> = emptyFlow(),
    val exploreMedia: List<MediaUIState> = emptyList(),
    val isLoading: Boolean = false,
    val error: List<ErrorUIState> = emptyList()
)
