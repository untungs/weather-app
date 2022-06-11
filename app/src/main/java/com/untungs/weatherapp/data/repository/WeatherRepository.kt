package com.untungs.weatherapp.data.repository

import com.untungs.weatherapp.network.NetworkDataSource
import com.untungs.weatherapp.network.model.WeatherDaily
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val networkDataSource: NetworkDataSource
) {
    suspend fun getWeatherDaily(lat: Float, lon: Float): WeatherDaily = withContext(dispatcher) {
        networkDataSource.getWeatherDaily(lat, lon)
    }
}