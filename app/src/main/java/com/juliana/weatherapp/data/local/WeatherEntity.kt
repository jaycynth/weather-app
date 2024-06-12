package com.juliana.weatherapp.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.juliana.weatherapp.domain.util.getDayOfWeekName

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "weather_id")
    val id: Int,
    val lat: Double,
    val lon: Double,
    val dt: Int,
    val temp: Double,
    val tempMin: Double,
    val tempMax: Double,
    val dayOfWeek: String = dt.toLong().getDayOfWeekName(),
    val weatherType: String,

    )
