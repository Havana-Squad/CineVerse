package com.karrar.movieapp.domain.mappers.savedList

import android.util.Log
import com.karrar.movieapp.BuildConfig
import com.karrar.movieapp.data.remote.response.SavedListDto
import com.karrar.movieapp.domain.mappers.Mapper
import com.karrar.movieapp.domain.models.SaveListDetails
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

class SaveListDetailsMapper @Inject constructor() : Mapper<SavedListDto, SaveListDetails> {
    override fun map(input: SavedListDto): SaveListDetails {
        return SaveListDetails(
            id = input.id ?: 0,
            mediaType = input.mediaType ?: "",
            title = listOf(input.originalTitle, input.originalName).first { it != null }.toString(),
            releaseDate = listOf(input.firstAirDate, input.releaseDate).first { it != null }.toFormattedDate(),
            voteAverage = input.voteAverage ?: 0.0,
            posterPath = BuildConfig.IMAGE_BASE_PATH + input.backdropPath,
        )
    }

    private fun String?.toFormattedDate(): String {
        if (this == null) return ""
        Log.d("dddd", this)
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy, MMM dd", Locale.ENGLISH)

        return LocalDate.parse(this, inputFormatter).format(outputFormatter)
    }
}