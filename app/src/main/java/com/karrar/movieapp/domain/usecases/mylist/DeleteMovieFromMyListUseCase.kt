package com.karrar.movieapp.domain.usecases.mylist

import com.karrar.movieapp.data.repository.AccountRepository
import com.karrar.movieapp.data.repository.MovieRepository
import com.karrar.movieapp.utilities.ErrorUI
import com.karrar.movieapp.utilities.checkIfExist
import javax.inject.Inject

class DeleteMovieFromMyListUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(listID: Int, mediaId: Int): String {
        val result = movieRepository.getListDetails(listID)
        return if (result?.checkIfExist(mediaId) == false) {
            "Fail: this movie is not in the list"
        } else {
            deleteMovieFromList(listID = listID, mediaId = mediaId)
        }
    }

    private suspend fun deleteMovieFromList(listID: Int, mediaId: Int): String {
        val sessionID = accountRepository.getSessionId()
        return sessionID?.let {
            movieRepository.deleteMovieFromCollection(
                sessionId = it,
                listId = listID,
                movieId = mediaId
            )
            "Success: The movie has been added"
        } ?: throw Throwable(ErrorUI.NO_LOGIN)
    }
}