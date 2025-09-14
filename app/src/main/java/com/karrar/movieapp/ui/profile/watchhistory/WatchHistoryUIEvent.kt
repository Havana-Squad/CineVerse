package com.karrar.movieapp.ui.profile.watchhistory

sealed interface WatchHistoryUIEvent {
    data class MovieEvent(val movie_id: Int) : WatchHistoryUIEvent
    data class TVShowEvent(val tvShowID: Int) : WatchHistoryUIEvent
    class NavigateExploreEvent(): WatchHistoryUIEvent
}