package com.untungs.weatherapp.common

import com.untungs.weatherapp.data.Location

sealed interface LoadingUiState<out T : Any> {
    data class Success<out T : Any>(val data: T) : LoadingUiState<T>
    data class Error(val message: String) : LoadingUiState<Nothing>
    object Loading : LoadingUiState<Nothing>
    object Unknown : LoadingUiState<Nothing>
}