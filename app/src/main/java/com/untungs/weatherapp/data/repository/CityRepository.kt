package com.untungs.weatherapp.data.repository

import com.untungs.weatherapp.data.CityLocation
import com.untungs.weatherapp.network.NetworkDataSource
import com.untungs.weatherapp.network.model.CityGeo
import com.untungs.weatherapp.network.model.toDomainModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CityRepository @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val networkDataSource: NetworkDataSource
) {
    suspend fun getCities(name: String): List<CityLocation> = withContext(dispatcher) {
        networkDataSource.getCities(name)
            .map(CityGeo::toDomainModel)
    }
}