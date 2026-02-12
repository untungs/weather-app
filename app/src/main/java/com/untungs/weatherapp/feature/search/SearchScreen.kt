package com.untungs.weatherapp.feature.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.untungs.weatherapp.common.EmptyScreen
import com.untungs.weatherapp.common.LoadingUiState
import com.untungs.weatherapp.data.Location
import com.untungs.weatherapp.ui.theme.WeatherAppTheme

@Composable
fun SearchRoute(
    onClickItem: (location: Location) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.loadingUiState.collectAsState()
    SearchScreen(uiState, onClickItem)
}

@Composable
fun SearchScreen(
    uiState: LoadingUiState<List<Location>>,
    onClickItem: (location: Location) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when (uiState) {
            is LoadingUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is LoadingUiState.Success -> {
                if (uiState.data.isEmpty()) {
                    EmptyScreen(text = "Location not found")
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding =
                            PaddingValues(
                                bottom =
                                    WindowInsets.navigationBars
                                        .asPaddingValues()
                                        .calculateBottomPadding()
                            )
                    ) { items(uiState.data) { SearchItem(it, onClickItem) } }
                }
            }

            is LoadingUiState.Error -> {
                Text(
                    text = uiState.message,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(48.dp)
                )
            }

            is LoadingUiState.Unknown -> {
                Text(
                    text = "Enter a city name",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(48.dp)
                )
            }
        }
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
        Divider()
    }
}

@Preview
@Composable
fun SearchPreview() {
    WeatherAppTheme {
        SearchScreen(LoadingUiState.Success(listOf(Location("Yogyakarta", 0f, 0f)))) {}
    }
}
