package com.karrar.movieapp.data.repository

interface TipRepository {
    suspend fun getCategoryTipStatus(): Boolean

    suspend fun closeCategoryTip()
}