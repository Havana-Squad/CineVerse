package com.karrar.movieapp.domain.usecases.tip

import com.karrar.movieapp.data.repository.TipRepository
import javax.inject.Inject

class CloseCategoryTipUseCase  @Inject constructor(
    private val tipRepository: TipRepository,
) {
    suspend operator fun invoke() = tipRepository.closeCategoryTip()
}