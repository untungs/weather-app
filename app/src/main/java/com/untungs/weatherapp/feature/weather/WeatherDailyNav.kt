package com.untungs.weatherapp.feature.weather

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.untungs.weatherapp.data.Location
import com.untungs.weatherapp.nav.WeatherDaily
import com.untungs.weatherapp.ui.component.AppBarState

fun NavGraphBuilder.weatherGraph(
    appBarState: AppBarState,
    snackbarHostState: SnackbarHostState,
) {
    composable<WeatherDaily> {
        val args = it.toRoute<WeatherDaily>()
        WeatherDailyRoute(args, appBarState, snackbarHostState)
    }
}

fun NavController.navigateToWeatherDaily(location: Location) = with(location) {
    navigate(WeatherDaily(city = name, lat = lat, lon = lon))
}
