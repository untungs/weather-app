package com.untungs.weatherapp.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import com.untungs.weatherapp.data.Weather
import com.untungs.weatherapp.data.WeatherStat

@Entity(
    tableName = "favorite_location",
    primaryKeys = ["lat", "lon"]
)
data class FavoriteLocationEntity(
    val lat: Float,
    val lon: Float,
    val locationName: String,
    @Embedded
    val currentWeather: CurrentWeatherDb
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

fun FavoriteLocationEntity.toDomain() = WeatherStat.CurrentWeatherStat(
    locationName,
    currentWeather.timestamp,
    currentWeather.weather.toDomain(),
    currentWeather.humidity,
    currentWeather.windSpeed,
    currentWeather.temp
)

fun WeatherDb.toDomain() = Weather(main, description, icon)