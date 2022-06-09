package com.untungs.weatherapp.network

import com.untungs.weatherapp.network.model.CityGeo

interface NetworkDataSource {

    suspend fun getCities(name: String): List<CityGeo>
}