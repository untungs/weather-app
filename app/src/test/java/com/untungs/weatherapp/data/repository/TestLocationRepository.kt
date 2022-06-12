package com.untungs.weatherapp.data.repository

import com.untungs.weatherapp.data.Location
import com.untungs.weatherapp.data.WeatherStat
import com.untungs.weatherapp.data.toDb
import com.untungs.weatherapp.local.entity.CurrentWeatherDb
import com.untungs.weatherapp.local.entity.FavoriteLocationEntity
import com.untungs.weatherapp.local.entity.WeatherDb
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map

class TestLocationRepository : LocationRepository {

    private val locationFlow: MutableSharedFlow<List<FavoriteLocationEntity>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override val favoriteLocations: Flow<List<FavoriteLocationEntity>>
        get() = locationFlow

    override suspend fun getCities(name: String): List<Location> {
        TODO("Not yet implemented")
    }

    override fun getFavoriteLocation(lat: Float, lon: Float): Flow<FavoriteLocationEntity?> {
        return favoriteLocations.map { list -> list.firstOrNull { it.lat == lat && it.lon == lon } }
    }

    override suspend fun addFavoriteLocation(
        lat: Float,
        lon: Float,
        name: String,
        currentWeather: WeatherStat.CurrentWeatherStat
    ) {
        val entity = FavoriteLocationEntity(lat, lon, name, currentWeather.toDb())
        locationFlow.tryEmit(listOf(entity))
    }

    override suspend fun removeFavoriteLocation(lat: Float, lon: Float) {
        TODO("Not yet implemented")
    }
}