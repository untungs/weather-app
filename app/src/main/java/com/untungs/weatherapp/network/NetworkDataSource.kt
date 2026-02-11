package com.untungs.weatherapp.network

import com.untungs.weatherapp.network.model.CityGeo
import com.untungs.weatherapp.network.model.NetworkForecast
import com.untungs.weatherapp.network.model.NetworkWeather

interface NetworkDataSource {

    suspend fun getCities(name: String): List<CityGeo>

    suspend fun getCurrentWeather(lat: Float, lon: Float): NetworkWeather

    suspend fun getForecast(lat: Float, lon: Float): NetworkForecast
}
