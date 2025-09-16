package com.karrar.movieapp.domain.mappers.savedList

import com.karrar.movieapp.BuildConfig
import com.karrar.movieapp.data.remote.response.SavedListDto
import com.karrar.movieapp.data.remote.response.genre.GenreDto
import com.karrar.movieapp.domain.models.SaveListDetails
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

class SaveListDetailsMapper @Inject constructor() {
    fun map(input: SavedListDto, genres: List<GenreDto>?): SaveListDetails {
        return SaveListDetails(
            id = input.id ?: 0,
            mediaType = input.mediaType ?: "",
            title = listOf(input.originalTitle, input.originalName).first { it != null }.toString(),
            releaseDate = listOf(input.firstAirDate, input.releaseDate)
                .first { it != null }
                .toFormattedDate(),
            voteAverage = input.voteAverage ?: 0.0,
            posterPath = BuildConfig.IMAGE_BASE_PATH + input.backdropPath,
            genres = genres?.let {
                if (genres.isEmpty()) listOf() else
                    input.genreIds?.map {
                        genres.first { genre ->
                            genre.id == it
                        }.name ?: ""
                    } ?: listOf()
            } ?: listOf(),
        )
    }

    private fun String?.toFormattedDate(): String {
        if (this == null) return ""
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy, MMM dd", Locale.ENGLISH)

        return LocalDate.parse(this, inputFormatter).format(outputFormatter)
    }
}