package com.untungs.weatherapp.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.untungs.weatherapp.R
import com.untungs.weatherapp.data.CityWeather
import com.untungs.weatherapp.data.FAKE_CITY

@Composable
fun HomeScreen() {
    Column() {
        CityWeatherItem(cityWeather = FAKE_CITY)
    }
}

@Composable
fun CityWeatherItem(cityWeather: CityWeather) {
    Card(modifier = Modifier.padding(8.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { /* onClick */ }
        ) {
            Row(modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 8.dp)) {
                Image(
                    modifier = Modifier.size(48.dp),
                    painter = painterResource(R.drawable.icon_02d),
                    contentDescription = null
                )
                Text(
                    modifier = Modifier.padding(8.dp, 0.dp, 0.dp, 0.dp),
                    text = with(cityWeather.city) { "$name, $state, $country" },
                    style = MaterialTheme.typography.h6
                )
            }
            Column(modifier = Modifier.padding(16.dp, 0.dp, 8.dp, 16.dp)) {
                with(cityWeather.weatherStat) {
                    Text(text = "Temperature: 30")
                    Text(text = "Humidity: $humidity")
                    Text(text = "Wind: $windSpeed")
                }
            }
        }
    }
}

@Preview
@Composable
fun CityWeatherItemPreview() {
    CityWeatherItem(FAKE_CITY)
}