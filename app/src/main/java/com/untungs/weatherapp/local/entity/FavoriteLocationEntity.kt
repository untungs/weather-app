package com.untungs.weatherapp.local.entity

import androidx.room.Embedded
import androidx.room.Entity

@Entity(
    tableName = "favorite_location",
    primaryKeys = ["lat", "lon"]
)
data class FavoriteLocationEntity(
    val lat: Float,
    val lon: Float,
    val locationName: String,
    @Embedded
    val currentWeather: CurrentWeatherDb?
)

data class CurrentWeatherDb(
    @Embedded
    val weather: WeatherDb,
    val humidity: Float,
    val windSpeed: Float,
    val temp: Float,
    val timestamp: Long
)

data class WeatherDb(
    val main: String,
    val description: String,
    val icon: String
)