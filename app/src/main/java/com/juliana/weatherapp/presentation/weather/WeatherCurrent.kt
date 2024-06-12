package com.juliana.weatherapp.presentation.weather

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.juliana.weatherapp.domain.weather.WeatherType
import com.juliana.weatherapp.presentation.ui.theme.cloudy
import com.juliana.weatherapp.presentation.ui.theme.rainy
import com.juliana.weatherapp.presentation.ui.theme.sunny


fun getBackgroundColor(weatherType: WeatherType): Color {
    return when (weatherType) {
        WeatherType.Sunny -> sunny
        WeatherType.Rainy -> rainy
        WeatherType.Cloudy -> cloudy

    }
}

@Composable
fun WeatherCurrent(
    state: WeatherState,
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    state.weatherData?.let { data ->

        val backgroundColor = getBackgroundColor(data.weatherType)

        Column(
            modifier = modifier
                .background(backgroundColor)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .fillMaxHeight(0.5f)
                    .fillMaxWidth()
            ) {


                Image(
                    painter = painterResource(id = data.weatherType.iconRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${data.temprature}°",
                        fontSize = 30.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = data.weatherType.weatherDesc.uppercase(),
                        fontSize = 18.sp,
                        color = Color.White
                    )
                }
            }


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                listOf(
                    data.tempratureMin to "min",
                    data.temprature to "Current",
                    data.tempratureMax to "max"
                ).forEach { (temp, label) ->
                    Column(
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "${temp}°",
                            fontSize = 14.sp,
                            color = Color.White
                        )
                        Text(
                            text = label,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 5.dp),
                thickness = 1.dp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))
            WeatherForecast(state = state)
        }

    }

}