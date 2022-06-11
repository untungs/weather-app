package com.untungs.weatherapp.feature.home

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.untungs.weatherapp.common.WeatherCard
import com.untungs.weatherapp.data.WeatherStat

@Composable
fun HomeRoute(viewModel: HomeViewModel = hiltViewModel()) {
    val locations by viewModel.favoriteLocations.collectAsState()
    HomeScreen(locations) {

    }
}

@Composable
fun HomeScreen(
    locations: List<WeatherStat.CurrentWeatherStat>,
    onRefresh: () -> Unit
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(false),
        onRefresh = onRefresh,
    ) {
        LazyColumn {
            items(locations) {
                WeatherCard(it)
            }
        }
    }
}