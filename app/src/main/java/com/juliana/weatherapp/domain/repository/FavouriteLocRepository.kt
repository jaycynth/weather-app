package com.juliana.weatherapp.domain.repository

import com.juliana.weatherapp.domain.favourites.FavouriteLocation
import kotlinx.coroutines.flow.Flow

interface FavouriteLocRepository {

    suspend fun insertFavouriteLocation(favouriteLocation: FavouriteLocation)

    suspend fun deleteFavouriteLocations(favouriteLocation: FavouriteLocation)

    fun getFavouriteLocations(): Flow<List<FavouriteLocation>>
}