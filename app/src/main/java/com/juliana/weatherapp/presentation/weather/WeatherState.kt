package com.juliana.weatherapp.presentation.weather

import com.juliana.weatherapp.domain.weather.WeatherData
import com.juliana.weatherapp.domain.weather.ForecastData

data class WeatherState(
    val weatherData: WeatherData? = null,
    val forecastInfo: ForecastData? = null,
    val isLoading:Boolean = false,
    val error:String? = null,
    val lat:Double?= null,
    val lon:Double? = null
    )


