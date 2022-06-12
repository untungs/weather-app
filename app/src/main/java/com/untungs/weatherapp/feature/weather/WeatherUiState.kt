package com.untungs.weatherapp.feature.weather

import com.untungs.weatherapp.data.WeatherDailyStat

data class WeatherUiState(
    val cityName: String,
    val weatherDaily: WeatherDailyStat? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isFavorite: Boolean = false,
    val favoriteChanged: Boolean? = null
)