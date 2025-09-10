package com.karrar.movieapp.ui.explore

import com.karrar.movieapp.ui.base.BaseInteractionListener
import com.karrar.movieapp.ui.explore.exploreUIState.GenreUIState
import com.karrar.movieapp.ui.explore.exploreUIState.MediaUIState
import com.karrar.movieapp.utilities.Constants

interface ExploreInteractionListener : BaseInteractionListener {
    fun onClickMedia(mediaItem: MediaUIState)
    fun onClickCategory(categoryId: Int)
    fun onClickMediaType(mediaTypeId: Int)
}