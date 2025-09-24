package com.karrar.movieapp.domain.usecases.searchUseCase

import com.karrar.movieapp.data.local.database.entity.SearchHistoryEntity
import com.karrar.movieapp.data.repository.MovieRepository
import javax.inject.Inject


class AddSearchHistoryItemUseCase @Inject constructor(
    private val movieRepository: MovieRepository
    ) {
    suspend operator fun invoke(query: String) {
        movieRepository.insertSearchItem(
            SearchHistoryEntity(search = query)
        )
    }
}