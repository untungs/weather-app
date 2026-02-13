package com.untungs.weatherapp.feature.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.untungs.weatherapp.common.LoadingUiState
import com.untungs.weatherapp.common.SOMETHING_WENT_WRONG
import com.untungs.weatherapp.data.repository.AppLocationRepository
import com.untungs.weatherapp.nav.Search
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.*

@HiltViewModel
class SearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val cityRepository: AppLocationRepository
) : ViewModel() {

    private val searchKey: String = savedStateHandle.toRoute<Search>().q

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
