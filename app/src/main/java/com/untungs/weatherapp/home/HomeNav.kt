package com.untungs.weatherapp.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.untungs.weatherapp.nav.AppNavigation

object HomeNav : AppNavigation {
    override val route: String = "home_route"
    override val destination: String = "home_destination"
}

fun NavGraphBuilder.homeGraph() {
    composable(route = HomeNav.route) {
        HomeScreen()
    }
}