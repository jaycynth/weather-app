package com.juliana.weatherapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather WHERE lat = :lat AND lon = :lon LIMIT 1")
    suspend fun getCurrentWeather(lat: Double, lon: Double): WeatherEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentWeather(weather: WeatherEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherForecast(weather: List<WeatherEntity>)

    @Query("SELECT * FROM weather WHERE strftime('%w', dt) IN ('1', '2', '3', '4', '5') ORDER BY dt")
    fun getWeekdayWeatherForecasts(): Flow<List<WeatherEntity>>
}