package com.karrar.movieapp.data.repository

import com.karrar.movieapp.data.local.AppConfiguration
import javax.inject.Inject

class TipRepositoryImp @Inject constructor(
    private val appConfiguration: AppConfiguration,
) : TipRepository, BaseRepository()  {
    override suspend fun getCategoryTipStatus(): Boolean {
        return appConfiguration.getCategoryTipStatus()
    }

    override suspend fun closeCategoryTip() {
        appConfiguration.closeCategoryTip()
    }
}