package com.untungs.weatherapp.feature.weather

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.untungs.weatherapp.data.repository.LocationRepository
import com.untungs.weatherapp.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class WeatherDailyViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val weatherRepository: WeatherRepository,
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val city: String = checkNotNull(savedStateHandle[WeatherDailyDestination.city])
    private val lat: Float = checkNotNull(savedStateHandle[WeatherDailyDestination.lat])
    private val lon: Float = checkNotNull(savedStateHandle[WeatherDailyDestination.lon])

    private val weatherUiState = MutableStateFlow(WeatherUiState(city))

    val uiState = weatherUiState
        .combine(locationRepository.getFavoriteLocation(lat, lon)) { uiState, location ->
            uiState.copy(isFavorite = location != null)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = weatherUiState.value
        )

    init {
        refreshWeather()
    }

    fun refreshWeather() {
        weatherUiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val weatherDaily = weatherRepository.getWeatherDaily(lat, lon)
                weatherUiState.update { it.copy(weatherDaily = weatherDaily, isLoading = false) }
            } catch (error: IOException) {
                weatherUiState.update { it.copy(errorMessage = error.message, isLoading = false) }
            }
        }
    }

    fun addFavorite() {
        val currentWeather = weatherUiState.value.weatherDaily?.current ?: return

        viewModelScope.launch {
            locationRepository.addFavoriteLocation(lat, lon, city, currentWeather)
        }
        weatherUiState.update { it.copy(favoriteChanged = true) }
    }

    fun removeFavorite() {
        viewModelScope.launch {
            locationRepository.removeFavoriteLocation(lat, lon)
        }
        weatherUiState.update { it.copy(favoriteChanged = false) }
    }

    fun onFavoriteChangeConsumed() {
        weatherUiState.update { it.copy(favoriteChanged = null) }
    }

    fun onDismissError() {
        weatherUiState.update {
            it.copy(errorMessage = null)
        }
    }
}