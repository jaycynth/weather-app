package com.juliana.weatherapp.presentation.favourites

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.MapStyleOptions
import com.juliana.weatherapp.domain.favourites.FavouriteLocation
import com.juliana.weatherapp.domain.repository.FavouriteLocRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MapsViewModel @Inject constructor(
    private val repository: FavouriteLocRepository,
) : ViewModel() {

    var state by mutableStateOf(MapState())

    init {
        viewModelScope.launch {
            repository.getFavouriteLocations().collectLatest { locations ->
                state = state.copy(
                    favouriteLocations = locations
                )
            }
        }
    }

    fun addFavouriteSpot(lat: Double, lon: Double) {
        viewModelScope.launch {
            repository.insertFavouriteLocation(
                FavouriteLocation(
                    lat,
                    lon
                )
            )
        }
    }

    fun onEvent(event: MapEvent) {
        when (event) {
            is MapEvent.ToggleFalloutMap -> {
                state = state.copy(
                    properties = state.properties.copy(
                        mapStyleOptions = if (state.isFalloutMap) {
                            null
                        } else MapStyleOptions(MapStyle.json),
                    ),
                    isFalloutMap = !state.isFalloutMap
                )
            }

            is MapEvent.OnMapLongClick -> {
                addFavouriteSpot(event.latLng.latitude, event.latLng.longitude)
            }

            is MapEvent.OnInfoWindowLongClick -> {
                viewModelScope.launch {
                    repository.deleteFavouriteLocations(event.favouriteLocation)
                }
            }
        }
    }
}