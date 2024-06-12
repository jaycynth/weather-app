package com.juliana.weatherapp.presentation.favourites

import com.google.android.gms.maps.model.LatLng
import com.juliana.weatherapp.domain.favourites.FavouriteLocation

sealed class MapEvent {
    data object ToggleFalloutMap: MapEvent()
    data class OnMapLongClick(val latLng: LatLng): MapEvent()
    data class OnInfoWindowLongClick(val favouriteLocation: FavouriteLocation): MapEvent()
}