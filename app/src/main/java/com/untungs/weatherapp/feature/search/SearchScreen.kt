package com.untungs.weatherapp.feature.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.untungs.weatherapp.R
import com.untungs.weatherapp.data.CityLocation
import com.untungs.weatherapp.ui.theme.WeatherAppTheme

@Composable
fun SearchRoute(
    onClickItem: (location: CityLocation) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.searchUiState.collectAsState()
    SearchScreen(uiState, onClickItem)
}

@Composable
fun SearchScreen(uiState: SearchUiState, onClickItem: (location: CityLocation) -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        when (uiState) {
            is SearchUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is SearchUiState.Success -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(uiState.cities) {
                        SearchItem(it, onClickItem)
                    }
                }
            }
            is SearchUiState.Error -> {
                Text(
                    text = uiState.message,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                        .padding(64.dp)
                )
            }
            is SearchUiState.Unknown -> {
                Text(
                    text = "Enter a city name",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                        .padding(64.dp)
                )
            }
        }

    }
}

@Composable
fun SearchItem(location: CityLocation, onClickItem: (location: CityLocation) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClickItem(location) }
    ) {
        Icon(
            modifier = Modifier.padding(8.dp),
            painter = painterResource(id = R.drawable.icon_02d),
            contentDescription = ""
        )
        Text(
            text = location.cityName,
            modifier = Modifier
                .padding(0.dp, 8.dp)
                .align(Alignment.CenterVertically)
        )
    }
}

@Preview
@Composable
fun SearchPreview() {
    WeatherAppTheme {
        SearchScreen(SearchUiState.Success(listOf(CityLocation("Yogyakarta", 0f, 0f)))) { }
    }
}