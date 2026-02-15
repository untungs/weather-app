package com.untungs.weatherapp.feature.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.untungs.weatherapp.common.EmptyScreen
import com.untungs.weatherapp.common.LoadingUiState
import com.untungs.weatherapp.data.Location
import com.untungs.weatherapp.ui.component.LocalScaffoldPadding
import com.untungs.weatherapp.ui.theme.WeatherAppTheme

@Composable
fun SearchRoute(
    onClickItem: (location: Location) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.loadingUiState.collectAsStateWithLifecycle()
    SearchScreen(uiState, onClickItem)
}

@Composable
fun SearchScreen(
    uiState: LoadingUiState<List<Location>>,
    onClickItem: (location: Location) -> Unit
) {
    if (uiState is LoadingUiState.Success && !uiState.data.isEmpty()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = LocalScaffoldPadding.current
        ) {
            items(uiState.data) { SearchItem(it, onClickItem) }
        }
    } else {
        EmptyScreen(text = "Enter a city name", successText = "Location not found", uiState)
    }
}

@Composable
fun SearchItem(location: Location, onClickItem: (location: Location) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClickItem(location) }
    ) {
        Text(
            text = location.name,
            modifier = Modifier.padding(12.dp)
        )
        HorizontalDivider()
    }
}

@Preview
@Composable
fun SearchPreview() {
    WeatherAppTheme {
        SearchScreen(LoadingUiState.Success(listOf(Location("Yogyakarta", 0f, 0f)))) {}
    }
}
