package com.untungs.weatherapp.data.repository

import com.untungs.weatherapp.data.Location
import com.untungs.weatherapp.data.WeatherStat
import com.untungs.weatherapp.local.entity.FavoriteLocationEntity
import kotlinx.coroutines.flow.Flow

interface LocationRepository {

    val favoriteLocations: Flow<List<FavoriteLocationEntity>>

    suspend fun getCities(name: String): List<Location>

    fun getFavoriteLocation(lat: Float, lon: Float): Flow<FavoriteLocationEntity?>

    suspend fun addFavoriteLocation(
        lat: Float,
        lon: Float,
        name: String,
        currentWeather: WeatherStat.CurrentWeatherStat
    )

    suspend fun removeFavoriteLocation(lat: Float, lon: Float)
}