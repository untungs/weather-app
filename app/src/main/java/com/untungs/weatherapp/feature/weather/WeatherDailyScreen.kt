package com.untungs.weatherapp.feature.weather

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.untungs.weatherapp.R
import com.untungs.weatherapp.network.model.Current
import com.untungs.weatherapp.network.model.WeatherDaily

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
            uiState.weatherDaily?.let {
                item {
                    WeatherCard(it.current)
                }
            }
        }
    }
}

@Composable
fun WeatherCard(current: Current) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = "Today",
                style = MaterialTheme.typography.h6
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .size(80.dp)
                        .padding(8.dp),
                    painter = painterResource(R.drawable.icon_02d),
                    contentDescription = null
                )
                Text(
                    text = "${current.temp}°",
                    style = MaterialTheme.typography.h2
                )
                Column(
                    modifier = Modifier.padding(16.dp, 8.dp)
                ) {
                    ProvideTextStyle(value = MaterialTheme.typography.subtitle1) {
                        Text(text = "Humidity: ${current.humidity}")
                        Text(text = "Wind: ${current.wind_speed}")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun WeatherCardPreview() {
    WeatherCard(Current(0, 30.57f, 0f,0f, emptyList()))
}