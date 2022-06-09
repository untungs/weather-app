package com.untungs.weatherapp.feature.search

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.untungs.weatherapp.nav.AppDestination

object SearchDestination : AppDestination {
    override val route: String = "search_route"
    override val destination: String = "search_destination"
    const val searchKey = "searchKey"
}

fun NavGraphBuilder.searchGraph(
    onSearchItemClick: () -> Unit
) {
    composable(
        route = "${SearchDestination.route}/{${SearchDestination.searchKey}}",
        arguments = listOf(
            navArgument(SearchDestination.searchKey) {
                type = NavType.StringType
            }
        )
    ) {
        SearchRoute()
    }
}