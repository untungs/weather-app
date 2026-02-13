package com.untungs.weatherapp.feature.search

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.untungs.weatherapp.data.Location
import com.untungs.weatherapp.nav.Search

fun NavGraphBuilder.searchGraph(
    contentPadding: PaddingValues,
    onSearchItemClick: (location: Location) -> Unit
) {
    composable<Search> {
        SearchRoute(contentPadding, onSearchItemClick)
    }
}

fun NavController.navigateToSearch(searchKey: String = "") {
    navigate(Search(q = searchKey)) {
        popUpTo<Search> {
            inclusive = true
        }
    }
}
