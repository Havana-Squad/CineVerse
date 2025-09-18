package com.karrar.movieapp.di

import com.karrar.movieapp.data.local.AppConfiguration
import com.karrar.movieapp.data.local.AppConfigurator
import com.karrar.movieapp.data.local.AppPreferences
import com.karrar.movieapp.data.local.AppPreferencesImpl
import com.karrar.movieapp.data.local.TipConfiguration
import com.karrar.movieapp.data.local.TipConfigurator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataSourceModule {

    @Singleton
    @Binds
    abstract fun bindAppConfiguration(appConfigurator: AppConfigurator) :AppConfiguration

    @Singleton
    @Binds
    abstract fun bindAppPreference(appPreferencesImpl: AppPreferencesImpl): AppPreferences


    @Singleton
    @Binds
    abstract fun bindTipConfiguration(tipConfigurator: TipConfigurator) : TipConfiguration
}