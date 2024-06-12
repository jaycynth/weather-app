package com.juliana.weatherapp.presentation.weather

import android.location.Location
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juliana.weatherapp.domain.location.LocationTracker
import com.juliana.weatherapp.domain.repository.WeatherRepository
import com.juliana.weatherapp.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val locationTracker: LocationTracker,
) : ViewModel() {

    var state by mutableStateOf(WeatherState())


    fun loadWeather() {
        viewModelScope.launch {
            setLoadingState()
            locationTracker.getCurrentLocation()?.let { location ->
                loadForecast(location)
                loadCurrentWeather(location)
                loadCurrentLocation(location)
            } ?: run {
                setErrorState("Couldn't retrieve location. Enable GPS.")
            }
        }
    }

    private fun loadCurrentLocation(location: Location) {
        viewModelScope.launch {
            state = state.copy(
                lat = location.latitude,
                lon = location.longitude,
                error = null
            )
        }
    }


    private fun setLoadingState() {
        state = state.copy(
            isLoading = true,
            error = null
        )
    }

    private fun setErrorState(message: String) {
        state = state.copy(
            isLoading = false,
            error = message
        )
    }

    private suspend fun loadForecast(location: Location) {
        repository.forecast(location.latitude, location.longitude).collect { result ->
            handleResult(result) { forecastInfo ->
                state = state.copy(
                    forecastInfo = forecastInfo,
                    isLoading = false,
                    error = null
                )
            }
        }
    }

    private suspend fun loadCurrentWeather(location: Location) {
        repository.currentWeather(location.latitude, location.longitude).collect { result ->
            handleResult(result) { weatherData ->
                state = state.copy(
                    weatherData = weatherData,
                    isLoading = false,
                    error = null
                )
            }
        }
    }

    private fun <T> handleResult(result: Resource<T>, onSuccess: (T?) -> Unit) {
        when (result) {
            is Resource.Loading -> setLoadingState()
            is Resource.Success -> onSuccess(result.data)
            is Resource.Error -> {
                state = state.copy(
                    isLoading = false,
                    error = result.message
                )
            }
        }
    }

}
