package com.untungs.weatherapp.feature.weather

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.untungs.weatherapp.data.CityLocation
import com.untungs.weatherapp.nav.AppDestination

object WeatherDailyDestination : AppDestination {
    val path: String = "weather_daily"
    const val city = "city"
    const val lat = "lat"
    const val lon = "lon"
    override val route: String = "$path?city={$city}&lat={$lat}&lon={$lon}"
}

fun NavGraphBuilder.weatherGraph() {
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
        WeatherDailyRoute()
    }
}

fun NavController.navigateToWeatherDaily(cityLocation: CityLocation) = with(cityLocation) {
    navigate("${WeatherDailyDestination.path}?city=${cityName}&lat=$lat&lon=$lon")
}