package com.untungs.weatherapp.feature.weather

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.untungs.weatherapp.common.LoadingUiState
import com.untungs.weatherapp.common.SOMETHING_WENT_WRONG
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
    private val loadingUiState = MutableStateFlow<LoadingUiState<Unit>>(LoadingUiState.Unknown)

    val uiState = weatherUiState
        .combine(locationRepository.getFavoriteLocation(lat, lon)) { uiState, location ->
            uiState.copy(isFavorite = location != null)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = weatherUiState.value
        )

    val loadingState = loadingUiState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000, 0),
            initialValue = loadingUiState.value
        )

    init {
        refreshWeather(fromUser = false)
    }

    fun refreshWeather(fromUser: Boolean = true) {
        if (fromUser) {
            loadingUiState.update { LoadingUiState.Loading }
        }

        viewModelScope.launch {
            try {
                val weatherDaily = weatherRepository.getWeatherDaily(lat, lon)
                weatherUiState.update { it.copy(weatherDaily = weatherDaily) }

                if (fromUser) {
                    loadingUiState.update { LoadingUiState.Success(Unit) }
                }
            } catch (error: IOException) {
                loadingUiState.update { LoadingUiState.Error(error.message ?: SOMETHING_WENT_WRONG) }
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

    fun onDismissSnackbar() {
        loadingUiState.update {
            LoadingUiState.Unknown
        }
    }
}