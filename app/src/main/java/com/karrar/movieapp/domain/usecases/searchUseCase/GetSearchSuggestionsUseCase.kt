package com.karrar.movieapp.domain.usecases.searchUseCase

import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import com.karrar.movieapp.data.repository.MovieRepository
import com.karrar.movieapp.domain.mappers.movie.MovieMapper
import com.karrar.movieapp.domain.models.Media
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetSearchSuggestionsUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
    private val movieMapper: MovieMapper
) {
    suspend operator fun invoke(query: String): List<Media> {
        return movieRepository.getSearchSuggestions(query).map(movieMapper::map)
    }
}