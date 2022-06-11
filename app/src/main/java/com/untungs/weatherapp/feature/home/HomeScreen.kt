package com.untungs.weatherapp.feature.home

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.untungs.weatherapp.common.LoadingUiState
import com.untungs.weatherapp.common.WeatherCard
import com.untungs.weatherapp.data.Location
import com.untungs.weatherapp.data.LocationWithCurrentWeather
import kotlinx.coroutines.launch

@Composable
fun HomeRoute(
    snackbarHostState: SnackbarHostState,
    onClickLocation: (Location) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val locations by viewModel.favoriteLocations.collectAsState()
    val scope = rememberCoroutineScope()

    val message = uiState.let {
        when (it) {
            is LoadingUiState.Success -> "Current weather updated"
            is LoadingUiState.Error -> it.message
            else -> null
        }
    }

    if (message != null) {
        LaunchedEffect(snackbarHostState, message) {
            scope.launch {
                val result = snackbarHostState.showSnackbar(message)
                if (result == SnackbarResult.Dismissed) {
                    viewModel.onDismissError()
                }
            }
        }
    }

    HomeScreen(uiState, locations, onClickLocation) {
        viewModel.refreshCurrentWeather()
    }
}

@Composable
fun HomeScreen(
    uiState: LoadingUiState<Unit>,
    locations: List<LocationWithCurrentWeather>,
    onClickLocation: (Location) -> Unit,
    onRefresh: () -> Unit
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(uiState is LoadingUiState.Loading),
        onRefresh = onRefresh,
    ) {
        LazyColumn {
            items(locations) {
                WeatherCard(it, onClickLocation)
            }
        }
    }
}