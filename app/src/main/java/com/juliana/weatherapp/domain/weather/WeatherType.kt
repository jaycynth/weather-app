package com.juliana.weatherapp.domain.weather

import androidx.annotation.DrawableRes
import com.juliana.weatherapp.R

sealed class WeatherType(
    val weatherDesc: String,
    @DrawableRes val iconRes: Int,
) {
    data object Sunny : WeatherType(
        weatherDesc = "Sunny",
        iconRes = R.drawable.forest_sunny
    )

    data object Rainy : WeatherType(
        weatherDesc = "Rainy",
        iconRes = R.drawable.forest_rainy
    )

    data object Cloudy : WeatherType(
        weatherDesc = "Cloudy",
        iconRes = R.drawable.forest_cloudy
    )
}