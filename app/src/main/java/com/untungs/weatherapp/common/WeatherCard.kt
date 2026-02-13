package com.untungs.weatherapp.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.untungs.weatherapp.R
import com.untungs.weatherapp.data.Weather
import com.untungs.weatherapp.data.WeatherStat

@Composable
fun WeatherCard(
    titleCard: String,
    stat: WeatherStat,
    modifier: Modifier = Modifier,
    onClick: Function? = null,
    bottomContent: @Composable (() -> Unit)? = null
) {
    val cardModifier = modifier
        .fillMaxWidth()
        .padding(8.dp, 8.dp, 8.dp, 0.dp)

    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = cardModifier,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceBright),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            CardContent(titleCard, stat, bottomContent)
        }
    } else {
        Card(
            modifier = cardModifier,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceBright),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            CardContent(titleCard, stat, bottomContent)
        }
    }
}

@Composable
private fun CardContent(
    titleCard: String,
    stat: WeatherStat,
    bottomContent: @Composable (() -> Unit)?
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = titleCard,
            modifier = Modifier.padding(8.dp, 0.dp),
            style = MaterialTheme.typography.titleLarge
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AsyncImage(
                model = "https://openweathermap.org/img/wn/${stat.weather.icon}@4x.png",
                modifier = Modifier
                    .size(80.dp)
                    .padding(8.dp),
                contentDescription = "Weather Icon"
            )
            when (stat) {
                is WeatherStat.CurrentWeatherStat -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.device_thermostat),
                            modifier = Modifier
                                .size(36.dp)
                                .padding(4.dp),
                            contentDescription = "Temp"
                        )
                        Text(
                            text = "${stat.temp}°",
                            style = MaterialTheme.typography.displayMedium
                        )
                    }
                }

                is WeatherStat.DailyWeatherStat -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.device_thermostat),
                            modifier = Modifier
                                .size(36.dp)
                                .padding(4.dp),
                            contentDescription = "Temp"
                        )
                        Text(
                            text = "${stat.temp}°",
                            style = MaterialTheme.typography.displaySmall
                        )
                    }
                }
            }
            Column(
                modifier = Modifier.padding(16.dp, 8.dp)
            ) {
                CompositionLocalProvider(
                    LocalTextStyle provides MaterialTheme.typography.labelMedium
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.humidity_mid),
                            modifier = Modifier
                                .size(24.dp)
                                .padding(4.dp),
                            contentDescription = "Humidity"
                        )
                        Text(text = "Humidity: ${stat.humidity}")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.wind_power),
                            modifier = Modifier
                                .size(24.dp)
                                .padding(4.dp),
                            contentDescription = "Wind Speed"
                        )
                        Text(text = "Wind: ${stat.windSpeed}")
                    }
                }
            }
        }

        bottomContent?.invoke()
    }
}

@Preview
@Composable
fun WeatherCardPreview() {
    val weather = Weather("", "", "")
    WeatherCard("Today", WeatherStat.CurrentWeatherStat("Today", 0, weather, 30.57f, 10.08f, 30f))
}

@Preview
@Composable
fun WeatherCardDailyPreview() {
    val weather = Weather("", "", "")
    WeatherCard("Monday", WeatherStat.DailyWeatherStat("Monday", 0, weather, 30.57f, 10.08f, 30.4f))
}
