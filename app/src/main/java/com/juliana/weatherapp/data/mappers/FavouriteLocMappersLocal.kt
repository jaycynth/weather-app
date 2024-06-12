package com.juliana.weatherapp.data.mappers

import com.juliana.weatherapp.data.local.FavouriteLocationEntity
import com.juliana.weatherapp.domain.favourites.FavouriteLocation


fun FavouriteLocationEntity.toFavouriteLocation(): FavouriteLocation {
    return FavouriteLocation(
        lat = lat,
        lng = lng,
        id = id
    )
}

fun FavouriteLocation.toFavouriteLocationEntity(): FavouriteLocationEntity {
    return FavouriteLocationEntity(
        lat = lat,
        lng = lng,
        id = id
    )
}