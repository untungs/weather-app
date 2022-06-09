package com.untungs.weatherapp.feature.search

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.untungs.weatherapp.nav.AppDestination

object SearchDestination : AppDestination {
    private const val path = "search_route"
    const val searchKey = "searchKey"
    override val route: String = "$path?q={$searchKey}"

    fun route(searchKey: String): String {
        return "$path?q={$searchKey}"
    }
}

fun NavGraphBuilder.searchGraph(
    onSearchItemClick: () -> Unit
) {
    composable(
        route = SearchDestination.route,
        arguments = listOf(
            navArgument(SearchDestination.searchKey) {
                type = NavType.StringType
                nullable = true
            }
        )
    ) {
        SearchRoute()
    }
}

fun NavController.navigateToSearch(searchKey: String? = null) {
    navigate(searchKey?.let {  SearchDestination.route(it) } ?: SearchDestination.route)  {
        popUpTo(SearchDestination.route) {
            inclusive = true
        }
    }
}