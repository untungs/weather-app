package com.untungs.weatherapp.feature.home

import androidx.compose.material.SnackbarHostState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.untungs.weatherapp.data.Location
import com.untungs.weatherapp.nav.AppDestination

object HomeDestination : AppDestination {
    override val route: String = "home_route"
}

fun NavGraphBuilder.homeGraph(
    snackbarHostState: SnackbarHostState,
    onClickLocation: (Location) -> Unit
) {
    composable(route = HomeDestination.route) {
        HomeRoute(snackbarHostState, onClickLocation)
    }
}