package com.karrar.movieapp.domain.mappers

import com.karrar.movieapp.data.local.database.entity.WatchHistoryEntity
import com.karrar.movieapp.ui.profile.watchhistory.MediaHistoryUiState
import javax.inject.Inject

class WatchHistoryMapper @Inject constructor() : Mapper<WatchHistoryEntity, MediaHistoryUiState> {
    override fun map(input: WatchHistoryEntity): MediaHistoryUiState {
        return MediaHistoryUiState(
            input.id,
            input.posterPath,
            input.movieTitle,
            input.voteAverage,
            input.releaseDate,
            input.movieDuration,
            input.mediaType,
            input.genres
        )
    }
    fun mapToEntity(input: MediaHistoryUiState): WatchHistoryEntity {
        return WatchHistoryEntity(
            id = input.id,
            posterPath = input.posterPath,
            movieTitle = input.movieTitle,
            voteAverage = input.voteAverage,
            releaseDate = input.releaseDate,
            movieDuration = input.movieDuration,
            mediaType = input.mediaType,
            genres = input.genres
        )
    }
}