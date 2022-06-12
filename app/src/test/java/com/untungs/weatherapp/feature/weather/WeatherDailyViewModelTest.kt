package com.untungs.weatherapp.feature.weather

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.untungs.weatherapp.MainDispatcherRule
import com.untungs.weatherapp.data.model.testCurrentWeather
import com.untungs.weatherapp.data.repository.TestLocationRepository
import com.untungs.weatherapp.data.repository.TestWeatherRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertTrue

class WeatherDailyViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val weatherRepository = TestWeatherRepository()
    private val locationRepository = TestLocationRepository()
    private lateinit var viewModel: WeatherDailyViewModel

    @Before
    fun setUp() {
        viewModel = WeatherDailyViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    WeatherDailyDestination.city to "Yogyakarta",
                    WeatherDailyDestination.lat to -7.8011945f,
                    WeatherDailyDestination.lon to 110.364917f
                )
            ),
            weatherRepository = weatherRepository,
            locationRepository = locationRepository
        )
    }

    @Test
    fun uiState_whenLocationIsStored_markedAsFavorite() = runTest {
        viewModel.uiState.test {
            awaitItem()
            locationRepository.addFavoriteLocation(
                -7.8011945f,
                110.364917f,
                "Yogyakarta",
                testCurrentWeather
            )
            val state = awaitItem()

            assertTrue(state.isFavorite)
        }
    }
}
