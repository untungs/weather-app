package com.untungs.weatherapp.network

import com.untungs.weatherapp.network.model.CityGeo
import com.untungs.weatherapp.network.model.WeatherDaily

interface NetworkDataSource {

    suspend fun getCities(name: String): List<CityGeo>

    suspend fun getWeatherDaily(lat: Float, lon: Float): WeatherDaily
}