package com.untungs.weatherapp.feature.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.untungs.weatherapp.nav.AppDestination

object HomeDestination : AppDestination {
    override val route: String = "home_route"
}

fun NavGraphBuilder.homeGraph() {
    composable(route = HomeDestination.route) {
        HomeRoute()
    }
}