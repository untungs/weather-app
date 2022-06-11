package com.untungs.weatherapp.feature.weather

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.untungs.weatherapp.common.WeatherCard
import kotlinx.coroutines.launch

@Composable
fun WeatherDailyRoute(
    snackbarHostState: SnackbarHostState,
    viewModel: WeatherDailyViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    uiState.errorMessage?.let {
        LaunchedEffect(snackbarHostState) {
            scope.launch {
                val result = snackbarHostState.showSnackbar(message = it)
                if (result == SnackbarResult.Dismissed) {
                    viewModel.onDismissError()
                }
            }
        }
    }

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
                    WeatherCard(stat.current.day, stat.current)
                }
                items(stat.daily) {
                    WeatherCard(it.day, it)
                }
            }
        }
    }
}