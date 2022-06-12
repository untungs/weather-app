package com.untungs.weatherapp.data

import com.untungs.weatherapp.local.entity.CurrentWeatherDb
import com.untungs.weatherapp.local.entity.FavoriteLocationEntity
import com.untungs.weatherapp.local.entity.WeatherDb

data class CityWeather(
    val city: City,
    val weatherStat: WeatherStat
)

data class City(
    val name: String,
    val state: String,
    val country: String,
    val lat: Float,
    val long: Float
)

data class WeatherDailyStat(
    val current: WeatherStat.CurrentWeatherStat,
    val daily: List<WeatherStat.DailyWeatherStat>
)

sealed interface WeatherStat {
    val day: String
    val timestamp: Long
    val weather: Weather
    val humidity: Float
    val windSpeed: Float

    data class CurrentWeatherStat(
        override val day: String,
        override val timestamp: Long,
        override val weather: Weather,
        override val humidity: Float,
        override val windSpeed: Float,
        val temp: Float
    ) : WeatherStat

    data class DailyWeatherStat(
        override val day: String,
        override val timestamp: Long,
        override val weather: Weather,
        override val humidity: Float,
        override val windSpeed: Float,
        val temp: Temp
    ) : WeatherStat
}

data class Weather(
    val main: String,
    val description: String,
    val icon: String
)

data class Temp(
    val day: Float,
    val night: Float
)

fun WeatherStat.CurrentWeatherStat.toDb() = CurrentWeatherDb(
    WeatherDb(weather.main, weather.description, weather.icon),
    humidity,
    windSpeed,
    temp,
    timestamp
)