package com.juliana.weatherapp.data.repository

import android.util.Log
import com.juliana.weatherapp.BuildConfig
import com.juliana.weatherapp.data.local.WeatherDao
import com.juliana.weatherapp.data.mappers.toEntity
import com.juliana.weatherapp.data.mappers.toForecastEntities
import com.juliana.weatherapp.data.mappers.toForecastInfo
import com.juliana.weatherapp.data.mappers.toWeatherData
import com.juliana.weatherapp.data.mappers.toForecastData
import com.juliana.weatherapp.data.remote.AppService
import com.juliana.weatherapp.domain.repository.WeatherRepository
import com.juliana.weatherapp.domain.util.DataAccess
import com.juliana.weatherapp.domain.util.Resource
import com.juliana.weatherapp.domain.weather.WeatherData
import com.juliana.weatherapp.domain.weather.ForecastData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject


class WeatherRepositoryImpl @Inject constructor(
    private val api: AppService,
    private val weatherDao: WeatherDao,
) : WeatherRepository {

    override suspend fun currentWeather(lat: Double, lon: Double): Flow<Resource<WeatherData>> {
        return DataAccess.fetchData(
            fetchLocal = { weatherDao.getCurrentWeather(lat, lon)?.toWeatherData() },
            fetchRemote = { api.currentWeather(lat, lon, BuildConfig.API_KEY) },
            saveRemoteData = { weatherData ->
                weatherData.toWeatherData().let { weatherDao.insertCurrentWeather(it.toEntity()) }
            },
            mapRemoteToLocal = { it.toWeatherData() },
        )
    }


    override suspend fun forecast(lat: Double, lon: Double): Flow<Resource<ForecastData>> {
        return DataAccess.fetchData(
            fetchLocal = {
                weatherDao.getWeekdayWeatherForecasts().firstOrNull()?.toForecastInfo()
            },
            fetchRemote = { api.forecast(lat, lon, BuildConfig.API_KEY) },
            saveRemoteData = { forecastResp ->
                forecastResp.toForecastData().toForecastEntities().let {
                    weatherDao.insertWeatherForecast(it)
                }
            },
            mapRemoteToLocal = { it.toForecastData() }
        )
    }


}