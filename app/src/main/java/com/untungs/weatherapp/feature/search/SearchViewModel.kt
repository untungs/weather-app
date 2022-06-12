package com.untungs.weatherapp.feature.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.untungs.weatherapp.common.SOMETHING_WENT_WRONG
import com.untungs.weatherapp.common.LoadingUiState
import com.untungs.weatherapp.data.repository.AppLocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val cityRepository: AppLocationRepository
) : ViewModel() {

    private val searchKey: String = checkNotNull(savedStateHandle[SearchDestination.searchKey])

    val loadingUiState = flow {
        if (searchKey.isNotBlank()) {
            emit(LoadingUiState.Loading)
            emit(LoadingUiState.Success(cityRepository.getCities(searchKey)))
        }
    }
        .catch { emit(LoadingUiState.Error(it.message ?: SOMETHING_WENT_WRONG)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = LoadingUiState.Unknown
        )

}