package com.untungs.weatherapp.data.repository

import com.untungs.weatherapp.data.Location
import com.untungs.weatherapp.local.dao.FavoriteLocationDao
import com.untungs.weatherapp.local.entity.FavoriteLocationEntity
import com.untungs.weatherapp.network.NetworkDataSource
import com.untungs.weatherapp.network.model.CityGeo
import com.untungs.weatherapp.network.model.toDomainModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CityRepository @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val networkDataSource: NetworkDataSource,
    private val favoriteLocationDao: FavoriteLocationDao
) {

    val favoriteLocations: Flow<List<FavoriteLocationEntity>> = favoriteLocationDao.getFavoriteLocations()

    suspend fun getCities(name: String): List<Location> = withContext(dispatcher) {
        networkDataSource.getCities(name)
            .map(CityGeo::toDomainModel)
    }

    suspend fun addFavoriteLocation(favoriteLocation: FavoriteLocationEntity) =
        withContext(dispatcher) {
            favoriteLocationDao.insert(favoriteLocation)
        }
}