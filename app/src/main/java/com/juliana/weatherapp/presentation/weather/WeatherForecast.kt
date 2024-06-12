package com.juliana.weatherapp.presentation.weather

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.juliana.weatherapp.R
import com.juliana.weatherapp.domain.weather.WeatherData
import com.juliana.weatherapp.domain.weather.WeatherType


fun getWeatherImage(weatherType: WeatherType): Int {
    return when (weatherType) {
        WeatherType.Sunny -> R.drawable.partlysunny
        WeatherType.Rainy -> R.drawable.rain
        WeatherType.Cloudy -> R.drawable.clear
    }
}

@Composable
fun WeatherForecast(state: WeatherState, modifier: Modifier = Modifier) {
    state.forecastInfo?.let { data ->
        LazyColumn(modifier = modifier) {
            val weekdays = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")

            items(weekdays) { day ->
                data.weatherDataPerDay[day]?.let { weatherDataList ->
                    weatherDataList.forEach { weatherData ->
                        WeatherItem(weatherData = weatherData, day = day)
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherItem(weatherData: WeatherData, day: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp, horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,

    ) {
        Text(
            text = day,
            fontSize = 14.sp,
            color = Color.White,
            modifier = Modifier.weight(0.3f)
        )
        Image(
            painter = painterResource(id = getWeatherImage(weatherData.weatherType)),
            contentDescription = weatherData.weatherType.weatherDesc,
            modifier = Modifier.size(30.dp).weight(0.4f)

        )
        Text(
            text = "${weatherData.temprature}°",
            fontSize = 14.sp,
            color = Color.White,
                    modifier = Modifier.weight(0.3f)

        )
    }
}

