package com.untungs.weatherapp.feature.weather

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.untungs.weatherapp.common.LoadingUiState
import com.untungs.weatherapp.common.WeatherCard
import com.untungs.weatherapp.ui.component.AppBarState
import kotlinx.coroutines.launch

@Composable
fun WeatherDailyRoute(
    appBarState: AppBarState,
    snackbarHostState: SnackbarHostState,
    viewModel: WeatherDailyViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val loadingState by viewModel.loadingState.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(appBarState, uiState.isFavorite) {
        appBarState.action = if (uiState.isFavorite) {
            Icons.Filled.Favorite to { viewModel.removeFavorite() }
        } else {
            Icons.Filled.FavoriteBorder to { viewModel.addFavorite() }
        }
    }
    uiState.favoriteChanged?.let { isFavorite ->
        val message = if (isFavorite) "Added to Favorite" else "Removed from Favorite"
        Toast.makeText(LocalContext.current, message, Toast.LENGTH_SHORT).show()

        viewModel.onFavoriteChangeConsumed()
    }

    val message = loadingState.let {
        when (it) {
            is LoadingUiState.Success -> "Daily weather forecast updated"
            is LoadingUiState.Error -> it.message
            else -> null
        }
    }
    message?.let {
        LaunchedEffect(snackbarHostState) {
            scope.launch {
                viewModel.onLoadingStateConsumed()
                snackbarHostState.showSnackbar(message = it)
            }
        }
    }

    WeatherDailyScreen(
        uiState = uiState,
        isRefreshing = loadingState == LoadingUiState.Loading,
        onRefresh = viewModel::refreshWeather
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WeatherDailyScreen(
    uiState: WeatherUiState,
    isRefreshing: Boolean,
    onRefresh: () -> Unit
) {
    val pullRefreshState = rememberPullRefreshState(isRefreshing, onRefresh)

    Box(Modifier.pullRefresh(pullRefreshState)) {
        LazyColumn(
            contentPadding =
                PaddingValues(
                    bottom =
                        WindowInsets.navigationBars
                            .asPaddingValues()
                            .calculateBottomPadding()
                )
        ) {
            uiState.weatherDaily?.let { stat ->
                item {
                    WeatherCard(stat.current.day, stat.current)
                }
                items(stat.daily) {
                    WeatherCard(it.day, it)
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
        PullRefreshIndicator(isRefreshing, pullRefreshState, Modifier.align(Alignment.TopCenter))
    }
}
