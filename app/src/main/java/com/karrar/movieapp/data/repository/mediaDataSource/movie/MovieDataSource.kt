package com.karrar.movieapp.data.repository.mediaDataSource.movie

import android.util.Log
import com.karrar.movieapp.data.remote.response.MovieDto
import com.karrar.movieapp.data.remote.service.MovieService
import com.karrar.movieapp.data.repository.mediaDataSource.BasePagingSource
import javax.inject.Inject

class MovieDataSource @Inject constructor(
    private val service: MovieService
) : BasePagingSource<MovieDto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDto> {
        val pageNumber = params.key ?: 1
        return try {
            val response = service.getAllMovies(pageNumber)
            Log.d("Exploring", "load: code: ${response.code()}, message: ${response.message()}, body: ${response.body()}")
            LoadResult.Page(
                data = response.body()?.items as List<MovieDto>,
                prevKey = null,
                nextKey = response.body()?.page?.plus(1)
            )
        } catch (e: Throwable) {
            LoadResult.Error(e)
        }
    }
}