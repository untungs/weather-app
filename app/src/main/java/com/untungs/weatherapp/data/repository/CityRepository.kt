package com.untungs.weatherapp.data.repository

import com.untungs.weatherapp.data.Location
import com.untungs.weatherapp.data.WeatherStat
import com.untungs.weatherapp.local.dao.FavoriteLocationDao
import com.untungs.weatherapp.local.entity.CurrentWeatherDb
import com.untungs.weatherapp.local.entity.FavoriteLocationEntity
import com.untungs.weatherapp.local.entity.WeatherDb
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

    val favoriteLocations: Flow<List<FavoriteLocationEntity>> =
        favoriteLocationDao.getFavoriteLocations()

    suspend fun getCities(name: String): List<Location> = withContext(dispatcher) {
        networkDataSource.getCities(name)
            .map(CityGeo::toDomainModel)
    }

    fun getFavoriteLocation(lat: Float, lon: Float): Flow<FavoriteLocationEntity?> =
        favoriteLocationDao.getFavoriteLocationAsFlow(lat, lon)

    suspend fun addFavoriteLocation(
        lat: Float,
        lon: Float,
        name: String,
        currentWeather: WeatherStat.CurrentWeatherStat
    ) = withContext(dispatcher) {
        val currentWeatherDb = with(currentWeather) {
            CurrentWeatherDb(
                WeatherDb(weather.main, weather.description, weather.icon),
                humidity,
                windSpeed,
                temp,
                timestamp
            )
        }
        val entity = FavoriteLocationEntity(lat, lon, name, currentWeatherDb)

        favoriteLocationDao.insert(entity)
    }

    suspend fun removeFavoriteLocation(lat: Float, lon: Float) = withContext(dispatcher) {
        favoriteLocationDao.delete(lat, lon)
    }
}