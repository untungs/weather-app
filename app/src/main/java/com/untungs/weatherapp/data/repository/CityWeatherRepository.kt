package com.untungs.weatherapp.data.repository

import com.untungs.weatherapp.network.NetworkDataSource
import com.untungs.weatherapp.network.model.CityGeo
import javax.inject.Inject

class CityWeatherRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource
) {
    suspend fun getCities(name: String): List<CityGeo> = networkDataSource.getCities(name)
}