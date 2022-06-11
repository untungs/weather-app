package com.untungs.weatherapp.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.untungs.weatherapp.data.WeatherStat
import com.untungs.weatherapp.data.repository.CityRepository
import com.untungs.weatherapp.local.entity.FavoriteLocationEntity
import com.untungs.weatherapp.local.entity.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val cityRepository: CityRepository
) : ViewModel() {

    val favoriteLocations: StateFlow<List<WeatherStat.CurrentWeatherStat>> =
        cityRepository.favoriteLocations.map { list ->
            list.map(FavoriteLocationEntity::toDomain)
        }
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                emptyList()
            )


}