package com.juliana.weatherapp.presentation.favourites

import com.juliana.weatherapp.domain.favourites.FavouriteLocation
import com.google.maps.android.compose.MapProperties

data class MapState(
    val properties: MapProperties = MapProperties(),
    val favouriteLocations: List<FavouriteLocation> = emptyList(),
    val isFalloutMap: Boolean = false
)