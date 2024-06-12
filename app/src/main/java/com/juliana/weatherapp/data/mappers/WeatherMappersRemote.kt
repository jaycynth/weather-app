package com.juliana.weatherapp.data.mappers

import com.juliana.weatherapp.data.models.CurrentWeatherResponse
import com.juliana.weatherapp.data.models.ForecastResponse
import com.juliana.weatherapp.data.models.Weather
import com.juliana.weatherapp.domain.util.getDayOfWeekName
import com.juliana.weatherapp.domain.weather.WeatherData
import com.juliana.weatherapp.domain.weather.ForecastData
import com.juliana.weatherapp.domain.weather.WeatherType
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale


fun Weather.toWeatherType(): WeatherType? {
    return when {
        this.main.lowercase(Locale.ROOT).contains("sunny") -> WeatherType.Sunny
        this.main.lowercase(Locale.ROOT).contains("clouds") -> WeatherType.Cloudy
        this.main.lowercase(Locale.ROOT).contains("rain") -> WeatherType.Rainy
        else -> null
    }
}


fun CurrentWeatherResponse.toWeatherData(): WeatherData {
    val localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(this.dt.toLong()), ZoneId.systemDefault())

    val weatherType = this.weather.firstOrNull()?.toWeatherType()

    return WeatherData(
        time = localDateTime,
        tempratureMin = this.main.temp_min,
        tempratureMax = this.main.temp_max,
        temprature = this.main.temp,
        weatherType = weatherType!!,
        lat = this.coord.lat,
        lon = this.coord.lon,
        dayOfWeek = this.dt.toLong().getDayOfWeekName()
    )
}


fun ForecastResponse.toForecastData(): ForecastData {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())

    val weatherDataPerDay = mutableMapOf<String, MutableList<WeatherData>>()
    val weekdays = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")

    val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")


    this.list.forEach { forecast ->
        val date = dateFormat.parse(forecast.dt_txt)
        val dayOfWeek = dayFormat.format(date ?: return@forEach)

        if (dayOfWeek in weekdays) {
            val weatherData = WeatherData(
                tempratureMax = forecast.main.temp_max,
                tempratureMin = forecast.main.temp_min,
                temprature = forecast.main.temp,
                weatherType = forecast.weather[0].main.toWeatherType()!!,
                time = LocalDateTime.parse(forecast.dt_txt, dateTimeFormatter),
                lat =this.city.coord.lat,
                lon =this.city.coord.lon,
                dayOfWeek = forecast.dt.toLong().getDayOfWeekName()
                )
            if (!weatherDataPerDay.containsKey(dayOfWeek)) {
                weatherDataPerDay[dayOfWeek] = mutableListOf()
            }
            weatherDataPerDay[dayOfWeek]?.add(weatherData)
        }
    }

    return ForecastData(weatherDataPerDay)
}

