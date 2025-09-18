package com.karrar.movieapp.data.local

import javax.inject.Inject

interface TipConfiguration {

    suspend fun getCategoryTipStatus(): Boolean

    suspend fun closeCategoryTip()
}

class TipConfigurator @Inject constructor(private val dataStorePreferences: DataStorePreferences) :
    TipConfiguration {

    override suspend fun getCategoryTipStatus(): Boolean {
        return dataStorePreferences.readBoolean(IS_CATEGORY_TIP_SHOWN) ?: true
    }

    override suspend fun closeCategoryTip() {
        dataStorePreferences.writeBoolean(IS_CATEGORY_TIP_SHOWN, false)
    }

    companion object DataStorePreferencesKeys {
        const val IS_CATEGORY_TIP_SHOWN = "category_details_tip"
    }

}