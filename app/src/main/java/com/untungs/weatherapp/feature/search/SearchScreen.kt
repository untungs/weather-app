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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.untungs.weatherapp.R
import com.untungs.weatherapp.network.model.CityGeo
import com.untungs.weatherapp.ui.theme.WeatherAppTheme

@Composable
fun SearchRoute(viewModel: SearchViewModel = hiltViewModel()) {
    val uiState by viewModel.searchUiState.collectAsState()
    SearchScreen(uiState)
}

@Composable
fun SearchScreen(uiState: SearchUiState) {
    Box(modifier = Modifier.fillMaxSize()) {
        when (uiState) {
            is SearchUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is SearchUiState.Success -> {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(uiState.cities) {
                        SearchItem(it)
                    }
                }
            }
            is SearchUiState.Error -> {
                Text(
                    text = "Something went wrong",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            is SearchUiState.Unknown -> {
                Text(
                    text = "Search cities with the search bar above",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

    }
}

@Composable
fun SearchItem(cityGeo: CityGeo) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
    ) {
        Icon(
            modifier = Modifier.padding(8.dp),
            painter = painterResource(id = R.drawable.icon_02d),
            contentDescription = ""
        )
        Text(
            text = "${cityGeo.name}${cityGeo.state?.let { ", $it" } ?: ""}, ${cityGeo.country}",
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
        SearchScreen(
            SearchUiState.Success(listOf(CityGeo("Yogyakarta", 0f, 0f, "ID")))
        )
    }
}