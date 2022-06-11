package com.untungs.weatherapp.feature.weather

import com.untungs.weatherapp.network.model.WeatherDaily

data class WeatherUiState(
    val cityName: String,
    val weatherDaily: WeatherDaily? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)