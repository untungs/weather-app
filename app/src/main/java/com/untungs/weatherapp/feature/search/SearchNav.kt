package com.untungs.weatherapp.feature.search

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.untungs.weatherapp.data.Location
import com.untungs.weatherapp.nav.AppDestination

object SearchDestination : AppDestination {
    const val path = "search"
    const val searchKey = "searchKey"
    override val route: String = "$path?q={$searchKey}"
}

fun NavGraphBuilder.searchGraph(
    onSearchItemClick: (location: Location) -> Unit
) {
    composable(
        route = SearchDestination.route,
        arguments = listOf(
            navArgument(SearchDestination.searchKey) {
                type = NavType.StringType
                defaultValue = ""
            }
        ),
    ) {
        SearchRoute(onSearchItemClick)
    }
}

fun NavController.navigateToSearch(searchKey: String = "") {
    navigate("${SearchDestination.path}?q=$searchKey")  {
        popUpTo(SearchDestination.route) {
            inclusive = true
        }
    }
}