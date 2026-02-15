package com.untungs.weatherapp.feature.weather

import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import com.untungs.weatherapp.ui.component.LocalScaffoldPadding
import com.untungs.weatherapp.ui.component.PullToRefresh
import kotlinx.coroutines.launch

@Composable
fun WeatherDailyRoute(
    args: WeatherDaily,
    appBarState: AppBarState,
    snackbarHostState: SnackbarHostState,
    viewModel: WeatherDailyViewModel = hiltViewModel(
        creationCallback = { factory: WeatherDailyViewModel.WeatherDailyViewModelFactory ->
            factory.create(args)
        }
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val loadingState by viewModel.loadingState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    DisposableEffect(appBarState, uiState.isFavorite) {
        appBarState.action = {
            IconToggleButton(
                checked = uiState.isFavorite,
                onCheckedChange = {
                    if (it) viewModel.addFavorite() else viewModel.removeFavorite()
                }
            ) {
                Icon(
                    imageVector =
                        if (uiState.isFavorite) Icons.Filled.Favorite
                        else Icons.Filled.FavoriteBorder,
                    contentDescription =
                        if (uiState.isFavorite) "Remove from Favorite"
                        else "Add to Favorite"
                )
            }
        }

        onDispose {
            appBarState.action = null
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

@Composable
fun WeatherDailyScreen(
    uiState: WeatherUiState,
    isRefreshing: Boolean,
    onRefresh: () -> Unit
) {
    PullToRefresh(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
    ) {
        LazyColumn(contentPadding = LocalScaffoldPadding.current) {
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
