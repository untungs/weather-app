package com.untungs.weatherapp.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.untungs.weatherapp.common.EmptyScreen
import com.untungs.weatherapp.common.LoadingUiState
import com.untungs.weatherapp.common.WeatherCard
import com.untungs.weatherapp.data.Location
import com.untungs.weatherapp.data.LocationWithCurrentWeather
import kotlinx.coroutines.launch

@Composable
fun HomeRoute(
    snackbarHostState: SnackbarHostState,
    contentPadding: PaddingValues,
    onClickLocation: (Location) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val locations by viewModel.favoriteLocations.collectAsStateWithLifecycle()
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
                viewModel.onLoadingStateConsumed()
                snackbarHostState.showSnackbar(message)
            }
        }
    }

    HomeScreen(uiState, locations, contentPadding, onClickLocation, viewModel::removeFavorite) {
        viewModel.refreshCurrentWeather()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: LoadingUiState<Unit>,
    locations: List<LocationWithCurrentWeather>,
    contentPadding: PaddingValues,
    onClickLocation: (Location) -> Unit,
    onRemoveLocation: (Location) -> Unit,
    onRefresh: () -> Unit
) {
    if (locations.isEmpty()) {
        EmptyScreen(text = "Your favorite locations will be shown here")
    } else {
        val isRefreshing = uiState is LoadingUiState.Loading

        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = contentPadding
            ) {
                items(locations, key = { it.location.name }) {
                    WeatherCard(
                        titleCard = it.location.name,
                        stat = it.weather,
                        modifier = Modifier.animateItem(),
                        onClick = {
                            onClickLocation(it.location)
                        }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            OutlinedButton(onClick = { onRemoveLocation(it.location) }) {
                                Text(text = "Remove", color = MaterialTheme.colorScheme.error)
                            }
                            TextButton(onClick = { onClickLocation(it.location) }) {
                                Text(text = "Weather Forecast")
                            }
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(8.dp)) }
            }
        }
    }
}
