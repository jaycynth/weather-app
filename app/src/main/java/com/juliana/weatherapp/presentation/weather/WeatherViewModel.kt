package com.juliana.weatherapp.presentation.weather

import android.location.Location
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juliana.weatherapp.domain.location.LocationTracker
import com.juliana.weatherapp.domain.repository.WeatherRepository
import com.juliana.weatherapp.domain.util.Resource
import com.juliana.weatherapp.domain.weather.ForecastData
import com.juliana.weatherapp.domain.weather.WeatherData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val locationTracker: LocationTracker
) : ViewModel() {

    var state by mutableStateOf(WeatherState())
        private set

    init {
        loadWeather()
    }

    fun loadWeather() {
        viewModelScope.launch {
            setLoadingState()
            locationTracker.getCurrentLocation()?.let { location ->
                loadCurrentLocation(location)
                loadForecast(location)
                loadCurrentWeather(location)
            } ?: run {
                updateState(
                    isLoading = false,
                    error = "Couldn't retrieve location, Enable GPS"
                )
            }
        }
    }

    private fun loadCurrentLocation(location: Location) {
        viewModelScope.launch {
            updateState(
                lat = location.latitude,
                lon = location.longitude,
                error = null
            )
        }
    }

    private fun setLoadingState() {
        updateState(
            isLoading = true,
            error = null
        )
    }

    private suspend fun loadForecast(location: Location) {
        repository.forecast(location.latitude, location.longitude).collect { result ->
            handleResult(result, onSuccess = { forecastInfo ->
                updateState(
                    forecastInfo = forecastInfo,
                    isLoading = false,
                    error = null
                )
            }, onFailure = { message, forecastInfo ->
                updateState(
                    isLoading = false,
                    error = message,
                    forecastInfo = forecastInfo
                )
            })
        }
    }

    private suspend fun loadCurrentWeather(location: Location) {
        repository.currentWeather(location.latitude, location.longitude).collect { result ->
            handleResult(result, onSuccess = { weatherData ->
                updateState(
                    weatherData = weatherData,
                    isLoading = false,
                    error = null
                )
            }, onFailure = { message, weatherData ->
                updateState(
                    weatherData = weatherData,
                    isLoading = false,
                    error = message
                )
            })
        }
    }

    private fun <T> handleResult(
        result: Resource<T>,
        onSuccess: (T?) -> Unit,
        onFailure: (String, T?) -> Unit
    ) {
        when (result) {
            is Resource.Loading -> setLoadingState()
            is Resource.Success -> onSuccess(result.data)
            is Resource.Error -> onFailure(result.message, result.data as T)
        }
    }

    private fun updateState(
        isLoading: Boolean = state.isLoading,
        error: String? = state.error,
        lat: Double? = state.lat,
        lon: Double? = state.lon,
        weatherData: WeatherData? = state.weatherData,
        forecastInfo: ForecastData? = state.forecastInfo
    ) {
        state = state.copy(
            isLoading = isLoading,
            error = error,
            lat = lat,
            lon = lon,
            weatherData = weatherData,
            forecastInfo = forecastInfo
        )
    }
}