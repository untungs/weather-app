package com.untungs.weatherapp.network.model

import kotlinx.serialization.Serializable

@Serializable
data class WeatherDaily(
    val lat: Float,
    val lon: Float,
    val timezone: String,
    val current: Current,
    val daily: List<Daily>
)

@Serializable
data class Current(
    val dt: Long,
    val temp: Float,
    val humidity: Float,
    val wind_speed: Float,
    val weather: List<Weather>
)

@Serializable
data class Daily(
    val dt: Long,
    val temp: Temp,
    val humidity: Float,
    val wind_speed: Float,
    val weather: List<Weather>
)

@Serializable
data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

@Serializable
data class Temp(
    val day: Float,
    val night: Float
)