package com.untungs.weatherapp.feature.search

import com.untungs.weatherapp.network.model.CityGeo

sealed interface SearchUiState {
    data class Success(val cities: List<CityGeo>) : SearchUiState
    object Error : SearchUiState
    object Loading : SearchUiState
    object Unknown : SearchUiState
}