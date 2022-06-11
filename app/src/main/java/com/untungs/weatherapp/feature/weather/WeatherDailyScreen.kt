package com.untungs.weatherapp.feature.weather

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.untungs.weatherapp.R
import com.untungs.weatherapp.data.Temp
import com.untungs.weatherapp.data.Weather
import com.untungs.weatherapp.data.WeatherStat
import com.untungs.weatherapp.network.model.Current

@Composable
fun WeatherDailyRoute(viewModel: WeatherDailyViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    WeatherDailyScreen(uiState = uiState, onRefresh = {
        viewModel.refreshWeather()
    })
}

@Composable
fun WeatherDailyScreen(
    uiState: WeatherUiState,
    onRefresh: () -> Unit
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(uiState.isLoading),
        onRefresh = onRefresh,
    ) {
        LazyColumn {
            uiState.weatherDaily?.let { stat ->
                item {
                    WeatherCard(stat.current)
                }
                items(stat.daily) {
                    WeatherCard(it)
                }
            }
        }
    }
}

@Composable
fun WeatherCard(stat: WeatherStat) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stat.day,
                modifier = Modifier.padding(8.dp, 0.dp),
                style = MaterialTheme.typography.h5
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
                                style = MaterialTheme.typography.h3
                            )
                        }
                    }
                    is WeatherStat.DailyWeatherStat -> {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.clear_day),
                                    modifier = Modifier
                                        .size(36.dp)
                                        .padding(4.dp),
                                    contentDescription = "Day"
                                )
                                Text(
                                    text = "${stat.temp.day}°",
                                    style = MaterialTheme.typography.h4
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.nightlight),
                                    modifier = Modifier
                                        .size(36.dp)
                                        .padding(4.dp),
                                    contentDescription = "Night"
                                )
                                Text(
                                    text = "${stat.temp.night}°",
                                    style = MaterialTheme.typography.h4
                                )
                            }
                        }
                    }
                }
                Column(
                    modifier = Modifier.padding(16.dp, 8.dp)
                ) {
                    ProvideTextStyle(value = MaterialTheme.typography.subtitle2) {
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
        }
    }
}

@Preview
@Composable
fun WeatherCardPreview() {
    val weather = Weather("", "", "")
    WeatherCard(WeatherStat.CurrentWeatherStat("Today", weather, 30.57f, 10.08f, 30f))
}

@Preview
@Composable
fun WeatherCardDailyPreview() {
    val weather = Weather("", "", "")
    WeatherCard(WeatherStat.DailyWeatherStat("Monday", weather, 30.57f, 10.08f, Temp(30.4f, 29.9f)))
}