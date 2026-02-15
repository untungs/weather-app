package com.untungs.weatherapp.feature.home

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.untungs.weatherapp.data.Location
import com.untungs.weatherapp.nav.Home

fun NavGraphBuilder.homeGraph(
    snackbarHostState: SnackbarHostState,
    onClickLocation: (Location) -> Unit
) {
    composable<Home> { HomeRoute(snackbarHostState, onClickLocation) }
}
