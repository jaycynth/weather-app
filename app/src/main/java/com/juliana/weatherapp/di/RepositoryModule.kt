package com.juliana.weatherapp.di

import com.juliana.weatherapp.data.repository.FavouriteLocRepositoryImpl
import com.juliana.weatherapp.data.repository.WeatherRepositoryImpl
import com.juliana.weatherapp.domain.repository.FavouriteLocRepository
import com.juliana.weatherapp.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(
        weatherRepositoryImpl: WeatherRepositoryImpl
    ): WeatherRepository

    @Binds
    @Singleton
    abstract fun bindFavouriteLocationRepository(
        favouriteLocationRepositoryImpl: FavouriteLocRepositoryImpl
    ): FavouriteLocRepository
}