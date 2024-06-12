package com.juliana.weatherapp.domain.favourites

data class FavouriteLocation(
    val lat: Double,
    val lng: Double,
    val id: Int? = null
)