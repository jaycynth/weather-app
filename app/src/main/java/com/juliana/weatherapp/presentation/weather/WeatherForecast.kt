package com.juliana.weatherapp.presentation.weather

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.util.Locale


fun getWeatherImage(weatherType: WeatherType): Int {
    return when (weatherType) {
        WeatherType.Sunny -> R.drawable.partlysunny
        WeatherType.Rainy -> R.drawable.rain
        WeatherType.Cloudy -> R.drawable.clear
    }
}

fun getOneWeatherDataPerDay(weatherDataPerDay: Map<String, List<WeatherData>>): List<Pair<String, WeatherData>> {
    val today = LocalDateTime.now().dayOfWeek

    val filteredData: MutableList<Pair<String, WeatherData>> = mutableListOf()

    DayOfWeek.values().toList()
        .sortedBy { dayOfWeek ->
            val dayOffset: Int = dayOfWeek.ordinal - today.ordinal
            if (dayOffset >= 0) dayOffset else dayOffset + 7
        }
        .forEach { day ->
            val weatherDataList: List<WeatherData>? = weatherDataPerDay[day.name.lowercase(Locale.ROOT)]
            weatherDataList?.firstOrNull()?.let { weatherData ->
                filteredData.add(day.name.lowercase(Locale.ROOT) to weatherData)
            }
        }
    return filteredData.toList()
}


@Composable
fun WeatherForecast(state: WeatherState, modifier: Modifier = Modifier) {
    state.forecastInfo?.let { data ->
        val filteredData = getOneWeatherDataPerDay(data.weatherDataPerDay)

        filteredData.forEach { (day, weatherData) ->
            WeatherItem(weatherData = weatherData, day = day, modifier = modifier)
        }
    }
}

@Composable
fun WeatherItem(weatherData: WeatherData, day: String, modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 10.dp),
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
            modifier = Modifier
                .size(30.dp)
                .weight(0.4f)

        )
        Text(
            text = "${weatherData.temprature}°",
            fontSize = 14.sp,
            color = Color.White,
            modifier = Modifier.weight(0.3f)

        )
    }
}

