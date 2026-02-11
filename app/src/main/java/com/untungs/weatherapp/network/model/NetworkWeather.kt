package com.untungs.weatherapp.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkWeather(
    val dt: Long,
    val main: Main,
    val weather: List<NetworkWeatherModel>,
    val wind: Wind,
    val clouds: Clouds? = null,
    val visibility: Int? = null,
    val pop: Float? = null, // Probability of precipitation (for forecast)
    val dt_txt: String? = null // For forecast
)

@Serializable
data class Main(
    val temp: Float,
    val feels_like: Float? = null,
    val temp_min: Float? = null,
    val temp_max: Float? = null,
    val pressure: Int? = null,
    val humidity: Float
)

@Serializable
data class Wind(val speed: Float, val deg: Int? = null, val gust: Float? = null)

@Serializable
data class Clouds(val all: Int)

@Serializable
data class NetworkWeatherModel(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)
