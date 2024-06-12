package com.juliana.weatherapp.domain.weather

import java.time.LocalDateTime

data class WeatherData(
    val time: LocalDateTime,
    val tempratureMin: Double,
    val tempratureMax: Double,
    val temprature: Double,
    val weatherType: WeatherType,
    val lat:Double,
    val lon:Double,
    val dayOfWeek: String,
)