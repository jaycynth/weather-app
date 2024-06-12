package com.juliana.weatherapp.domain.repository


import com.juliana.weatherapp.domain.util.Resource
import com.juliana.weatherapp.domain.weather.WeatherData
import com.juliana.weatherapp.domain.weather.ForecastData
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun currentWeather(lat: Double, lon: Double): Flow<Resource<WeatherData>>

    suspend fun forecast(lat: Double, lon: Double): Flow<Resource<ForecastData>>

}