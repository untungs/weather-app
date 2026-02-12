package com.untungs.weatherapp.feature.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.untungs.weatherapp.common.EmptyScreen
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
                viewModel.onLoadingStateConsumed()
                snackbarHostState.showSnackbar(message)
            }
        }
    }

    HomeScreen(uiState, locations, onClickLocation, viewModel::removeFavorite) {
        viewModel.refreshCurrentWeather()
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    uiState: LoadingUiState<Unit>,
    locations: List<LocationWithCurrentWeather>,
    onClickLocation: (Location) -> Unit,
    onRemoveLocation: (Location) -> Unit,
    onRefresh: () -> Unit
) {
    if (locations.isEmpty()) {
        EmptyScreen(text = "Your favorite locations will be shown here")
    } else {
        val isRefreshing = uiState is LoadingUiState.Loading
        val pullRefreshState = rememberPullRefreshState(isRefreshing, onRefresh)

        Box(Modifier.pullRefresh(pullRefreshState)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding =
                    PaddingValues(
                        bottom =
                            WindowInsets.navigationBars
                                .asPaddingValues()
                                .calculateBottomPadding()
                    )
            ) {
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
                item { Spacer(modifier = Modifier.height(8.dp)) }
            }
            PullRefreshIndicator(isRefreshing, pullRefreshState, Modifier.align(Alignment.TopCenter))
        }
    }
}
