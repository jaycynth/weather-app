package com.juliana.weatherapp.domain.weather

data class ForecastData(
    val weatherDataPerDay: Map<String, List<WeatherData>>,
)


