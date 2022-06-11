package com.untungs.weatherapp.feature.weather

import androidx.compose.material.SnackbarHostState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.untungs.weatherapp.data.Location
import com.untungs.weatherapp.nav.AppDestination

object WeatherDailyDestination : AppDestination {
    val path: String = "weather_daily"
    const val city = "city"
    const val lat = "lat"
    const val lon = "lon"
    override val route: String = "$path?city={$city}&lat={$lat}&lon={$lon}"
}

fun NavGraphBuilder.weatherGraph(snackbarHostState: SnackbarHostState) {
    composable(
        route = WeatherDailyDestination.route,
        arguments = listOf(
            navArgument(WeatherDailyDestination.city) {
                type = NavType.StringType
            },
            navArgument(WeatherDailyDestination.lat) {
                type = NavType.FloatType
            },
            navArgument(WeatherDailyDestination.lon) {
                type = NavType.FloatType
            }
        )
    ) {
        WeatherDailyRoute(snackbarHostState)
    }
}

fun NavController.navigateToWeatherDaily(location: Location) = with(location) {
    navigate("${WeatherDailyDestination.path}?city=${name}&lat=$lat&lon=$lon")
}