package com.juliana.weatherapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourites")
data class FavouriteLocationEntity(
    val lat: Double,
    val lng: Double,
    @PrimaryKey val id: Int? = null
)