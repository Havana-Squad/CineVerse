package com.karrar.movieapp.data.repository

import com.karrar.movieapp.data.local.TipConfiguration
import javax.inject.Inject

class TipRepositoryImp @Inject constructor(
    private val tipConfiguration: TipConfiguration,
) : TipRepository, BaseRepository()  {
    override suspend fun getCategoryTipStatus(): Boolean {
        return tipConfiguration.getCategoryTipStatus()
    }

    override suspend fun closeCategoryTip() {
        tipConfiguration.closeCategoryTip()
    }
}