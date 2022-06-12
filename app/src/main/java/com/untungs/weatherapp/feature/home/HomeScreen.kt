package com.untungs.weatherapp.feature.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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

    HomeScreen(uiState, locations, onClickLocation, viewModel::removeFavorite) {
        viewModel.refreshCurrentWeather()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    uiState: LoadingUiState<Unit>,
    locations: List<LocationWithCurrentWeather>,
    onClickLocation: (Location) -> Unit,
    onRemoveLocation: (Location) -> Unit,
    onRefresh: () -> Unit
) {
    if (locations.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Your favorite locations will be shown here",
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(48.dp),
                textAlign = TextAlign.Center
            )
        }
    } else {
        SwipeRefresh(
            state = rememberSwipeRefreshState(uiState is LoadingUiState.Loading),
            onRefresh = onRefresh,
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(locations, key = { it.location.name }) {
                    WeatherCard(
                        titleCard = it.location.name,
                        stat = it.weather,
                        modifier = Modifier
                            .animateItemPlacement()
                            .clickable {
                                onClickLocation(it.location)
                            }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            OutlinedButton(onClick = { onRemoveLocation(it.location) }) {
                                Text(text = "Remove", color = MaterialTheme.colors.secondaryVariant)
                            }
                            TextButton(onClick = { onClickLocation(it.location) }) {
                                Text(text = "Weather Forecast")
                            }
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}