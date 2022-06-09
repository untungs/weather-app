package com.untungs.weatherapp.feature.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.untungs.weatherapp.data.repository.CityWeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val cityWeatherRepository: CityWeatherRepository
) : ViewModel() {

    private val searchKey: String? = savedStateHandle[SearchDestination.searchKey]

    val searchUiState = flow {
        if (searchKey != null) {
            emit(SearchUiState.Loading)
            emit(SearchUiState.Success(cityWeatherRepository.getCities(searchKey)))
        }
    }
        .catch { emit(SearchUiState.Error) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SearchUiState.Unknown
        )

}