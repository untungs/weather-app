package com.untungs.weatherapp.feature.weather

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.untungs.weatherapp.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class WeatherDailyViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val city: String = checkNotNull(savedStateHandle[WeatherDailyDestination.city])
    private val lat: Float = checkNotNull(savedStateHandle[WeatherDailyDestination.lat])
    private val lon: Float = checkNotNull(savedStateHandle[WeatherDailyDestination.lon])

    private val weatherUiState = MutableStateFlow(WeatherUiState(city))

    val uiState = weatherUiState
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

    fun onDismissError() {
        weatherUiState.update {
            it.copy(errorMessage = null)
        }
    }
}