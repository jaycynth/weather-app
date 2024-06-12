package com.juliana.weatherapp.data.remote

import com.juliana.weatherapp.data.models.CurrentWeatherResponse
import com.juliana.weatherapp.data.models.ForecastResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AppService {

    @POST("weather")
    suspend fun currentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appId: String,
    ): CurrentWeatherResponse

    @GET("forecast")
    suspend fun forecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appId: String,
        @Query("cnt") count: Int = 5,
        @Query("units") units: String = "metric"
    ): ForecastResponse


}