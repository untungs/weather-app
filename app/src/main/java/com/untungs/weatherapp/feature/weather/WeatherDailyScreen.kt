package com.untungs.weatherapp.feature.weather

import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.untungs.weatherapp.common.LoadingUiState
import com.untungs.weatherapp.common.WeatherCard
import com.untungs.weatherapp.nav.WeatherDaily
import com.untungs.weatherapp.ui.component.AppBarState
import kotlinx.coroutines.launch

@Composable
fun WeatherDailyRoute(
    args: WeatherDaily,
    appBarState: AppBarState,
    snackbarHostState: SnackbarHostState,
    contentPadding: PaddingValues,
    viewModel: WeatherDailyViewModel = hiltViewModel(
        creationCallback = { factory: WeatherDailyViewModel.WeatherDailyViewModelFactory ->
            factory.create(args)
        }
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val loadingState by viewModel.loadingState.collectAsStateWithLifecycle()
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
        contentPadding = contentPadding,
        onRefresh = viewModel::refreshWeather
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherDailyScreen(
    uiState: WeatherUiState,
    isRefreshing: Boolean,
    contentPadding: PaddingValues,
    onRefresh: () -> Unit
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = contentPadding
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
    }
}
