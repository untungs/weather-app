package com.untungs.weatherapp.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.untungs.weatherapp.common.LoadingUiState
import com.untungs.weatherapp.common.SOMETHING_WENT_WRONG
import com.untungs.weatherapp.data.LocationWithCurrentWeather
import com.untungs.weatherapp.data.repository.CityRepository
import com.untungs.weatherapp.data.repository.WeatherRepository
import com.untungs.weatherapp.local.entity.FavoriteLocationEntity
import com.untungs.weatherapp.local.entity.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val cityRepository: CityRepository,
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    val favoriteLocations: StateFlow<List<LocationWithCurrentWeather>> =
        cityRepository.favoriteLocations.map { list ->
            list.map(FavoriteLocationEntity::toDomain)
        }
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                emptyList()
            )

    private val loadingUiState = MutableStateFlow<LoadingUiState<Unit>>(LoadingUiState.Unknown)
    val uiState = loadingUiState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = loadingUiState.value
        )

    fun refreshCurrentWeather() = viewModelScope.launch {
        loadingUiState.update { LoadingUiState.Loading }

        val errors = mutableListOf<Throwable?>()

        supervisorScope {
            favoriteLocations.value.forEach {
                launch {
                    errors += runCatching {
                        weatherRepository.updateWeather(it.location.lat, it.location.lon)
                    }.exceptionOrNull()
                }
            }
        }

        loadingUiState.update {
            errors.firstOrNull()?.let { LoadingUiState.Error(it.message ?: SOMETHING_WENT_WRONG) }
                ?: LoadingUiState.Success(Unit)
        }
    }

    fun onDismissError() {
        loadingUiState.update { LoadingUiState.Unknown }
    }
}