package com.juliana.weatherapp.data.mappers

import com.juliana.weatherapp.data.local.WeatherEntity
import com.juliana.weatherapp.domain.util.toInt
import com.juliana.weatherapp.domain.util.toLocalDateTime
import com.juliana.weatherapp.domain.weather.ForecastData
import com.juliana.weatherapp.domain.weather.WeatherData
import com.juliana.weatherapp.domain.weather.WeatherType
import java.util.Locale


fun String.toWeatherType(): WeatherType? {
    return when {
        this.lowercase(Locale.ROOT).contains("sunny") -> WeatherType.Sunny
        this.lowercase(Locale.ROOT).contains("clouds") -> WeatherType.Cloudy
        this.lowercase(Locale.ROOT).contains("rain") -> WeatherType.Rainy
        else -> null
    }
}

fun WeatherEntity.toWeatherData(): WeatherData {
    return WeatherData(
        lon = lon,
        lat = lat,
        time = dt.toLocalDateTime(),
        tempratureMin = tempMin,
        tempratureMax = tempMax,
        temprature = temp,
        weatherType = weatherType.toWeatherType()!!,
        dayOfWeek = dayOfWeek
    )
}

fun WeatherData.toEntity(): WeatherEntity {
    return WeatherEntity(
        id = 0,
        lat = this.lat,
        lon = this.lon,
        dt = this.time.toInt(),
        temp = this.temprature,
        tempMin = this.tempratureMin,
        tempMax = this.tempratureMax,
        weatherType = this.weatherType.weatherDesc
    )
}

fun List<WeatherEntity>.toForecastInfo(): ForecastData {
    val weatherDataMap = this.groupBy { it.dayOfWeek }
        .mapValues { entry ->
            entry.value.map { entity ->
                WeatherData(
                    temprature = entity.temp,
                    lat = entity.lat,
                    lon = entity.lon,
                    time = entity.dt.toLocalDateTime(),
                    tempratureMin = entity.tempMin,
                    tempratureMax = entity.tempMax,
                    dayOfWeek = entity.dayOfWeek,
                    weatherType = entity.weatherType.toWeatherType()!!

                )
            }
        }
    return ForecastData(weatherDataPerDay = weatherDataMap)
}

fun ForecastData.toForecastEntities(): List<WeatherEntity> {
    return this.weatherDataPerDay.flatMap { (dayOfWeek, weatherDataList) ->
        weatherDataList.map { weatherData ->
            WeatherEntity(
                id = 0,
                dayOfWeek = dayOfWeek,
                temp = weatherData.temprature,
                dt = weatherData.time.toInt(),
                lat = weatherData.lat,
                lon = weatherData.lon,
                tempMax = weatherData.tempratureMax,
                tempMin = weatherData.tempratureMin,
                weatherType = weatherData.weatherType.weatherDesc
            )
        }
    }
}
