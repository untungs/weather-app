package com.untungs.weatherapp.feature.weather

import app.cash.turbine.test
import com.untungs.weatherapp.MainDispatcherRule
import com.untungs.weatherapp.data.model.testCurrentWeather
import com.untungs.weatherapp.data.repository.TestLocationRepository
import com.untungs.weatherapp.data.repository.TestWeatherRepository
import com.untungs.weatherapp.nav.WeatherDaily
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class WeatherDailyViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val weatherRepository = TestWeatherRepository()
    private val locationRepository = TestLocationRepository()
    private lateinit var viewModel: WeatherDailyViewModel

    @Before
    fun setUp() {
        val args = WeatherDaily(city = "Yogyakarta", lat = -7.8011945f, lon = 110.364917f)
        viewModel = WeatherDailyViewModel(
            args = args,
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
