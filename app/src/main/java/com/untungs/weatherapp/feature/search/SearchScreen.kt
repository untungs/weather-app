package com.untungs.weatherapp.feature.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
    val cities by viewModel.cities.collectAsState()
    SearchScreen(cities)
}

@Composable
fun SearchScreen(cities: List<CityGeo>) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(cities) {
            SearchItem(it)
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
        SearchScreen(listOf(CityGeo("Yogyakarta", 0f, 0f, "ID")))
    }
}