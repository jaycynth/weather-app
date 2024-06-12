package com.juliana.weatherapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface FavouriteLocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavouriteLocation(locationEntity: FavouriteLocationEntity)

    @Delete
    suspend fun deleteFavouriteLocation(locationEntity: FavouriteLocationEntity)

    @Query("SELECT * FROM favourites")
    fun getFavouriteLocations(): Flow<List<FavouriteLocationEntity>>
}