package com.karrar.movieapp.data.repository

import com.karrar.movieapp.data.local.AppPreferences
import javax.inject.Inject

class TipRepositoryImp @Inject constructor(
    private val appPreferences: AppPreferences,
) : TipRepository, BaseRepository()  {
    override suspend fun getCategoryTipStatus(): Boolean {
        return appPreferences.getCategoryTipStatus()
    }

    override suspend fun closeCategoryTip() {
        appPreferences.closeCategoryTip()
    }
}