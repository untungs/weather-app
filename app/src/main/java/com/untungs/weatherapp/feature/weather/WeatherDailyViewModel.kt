package com.untungs.weatherapp.feature.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.untungs.weatherapp.common.LoadingUiState
import com.untungs.weatherapp.common.SOMETHING_WENT_WRONG
import com.untungs.weatherapp.data.repository.LocationRepository
import com.untungs.weatherapp.data.repository.WeatherRepository
import com.untungs.weatherapp.nav.WeatherDaily
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = WeatherDailyViewModel.WeatherDailyViewModelFactory::class)
class WeatherDailyViewModel @AssistedInject constructor(
    @Assisted private val args: WeatherDaily,
    private val weatherRepository: WeatherRepository,
    private val locationRepository: LocationRepository
) : ViewModel() {

    @AssistedFactory
    interface WeatherDailyViewModelFactory {
        fun create(args: WeatherDaily): WeatherDailyViewModel
    }

    private val weatherUiState = MutableStateFlow(WeatherUiState(args.city))
    private val loadingUiState = MutableStateFlow<LoadingUiState<Unit>>(LoadingUiState.Unknown)

    val uiState = weatherUiState
        .combine(locationRepository.getFavoriteLocation(args.lat, args.lon)) { uiState, location ->
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
                val weatherDaily = weatherRepository.getWeatherDaily(args.lat, args.lon)
                weatherUiState.update { it.copy(weatherDaily = weatherDaily) }

                if (fromUser) {
                    loadingUiState.update { LoadingUiState.Success(Unit) }
                }
            } catch (error: Exception) {
                loadingUiState.update {
                    LoadingUiState.Error(
                        error.message ?: SOMETHING_WENT_WRONG
                    )
                }
            }
        }
    }

    fun addFavorite() {
        val currentWeather = weatherUiState.value.weatherDaily?.current ?: return

        viewModelScope.launch {
            locationRepository.addFavoriteLocation(args.lat, args.lon, args.city, currentWeather)
        }
        weatherUiState.update { it.copy(favoriteChanged = true) }
    }

    fun removeFavorite() {
        viewModelScope.launch {
            locationRepository.removeFavoriteLocation(args.lat, args.lon)
        }
        weatherUiState.update { it.copy(favoriteChanged = false) }
    }

    fun onFavoriteChangeConsumed() {
        weatherUiState.update { it.copy(favoriteChanged = null) }
    }

    fun onLoadingStateConsumed() {
        loadingUiState.update {
            LoadingUiState.Unknown
        }
    }
}
