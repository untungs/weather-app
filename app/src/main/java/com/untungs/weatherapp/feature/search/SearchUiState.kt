package com.untungs.weatherapp.feature.search

import com.untungs.weatherapp.data.CityLocation

sealed interface SearchUiState {
    data class Success(val cities: List<CityLocation>) : SearchUiState
    data class Error(val message: String) : SearchUiState
    object Loading : SearchUiState
    object Unknown : SearchUiState
}