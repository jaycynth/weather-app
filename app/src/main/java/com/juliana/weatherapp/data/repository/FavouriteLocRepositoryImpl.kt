package com.juliana.weatherapp.data.repository

import com.juliana.weatherapp.data.local.FavouriteLocationDao
import com.juliana.weatherapp.data.mappers.toFavouriteLocation
import com.juliana.weatherapp.data.mappers.toFavouriteLocationEntity
import com.juliana.weatherapp.domain.favourites.FavouriteLocation
import com.juliana.weatherapp.domain.repository.FavouriteLocRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavouriteLocRepositoryImpl @Inject constructor(
    private val dao: FavouriteLocationDao,
) : FavouriteLocRepository {

    override suspend fun insertFavouriteLocation(favouriteLocation: FavouriteLocation) {
        dao.insertFavouriteLocation(favouriteLocation.toFavouriteLocationEntity())
    }

    override suspend fun deleteFavouriteLocations(favouriteLocation: FavouriteLocation) {
        dao.deleteFavouriteLocation(favouriteLocation.toFavouriteLocationEntity())
    }

    override fun getFavouriteLocations(): Flow<List<FavouriteLocation>> {
        return dao.getFavouriteLocations().map { spots ->
            spots.map { it.toFavouriteLocation() }
        }
    }
}