package com.untungs.weatherapp.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkForecast(val list: List<NetworkWeather>, val city: NetworkCity? = null)

@Serializable
data class NetworkCity(
    val id: Int,
    val name: String,
    val coord: NetworkCoord? = null,
    val country: String? = null,
    val population: Int? = null,
    val timezone: Int? = null,
    val sunrise: Long? = null,
    val sunset: Long? = null
)

@Serializable
data class NetworkCoord(val lat: Float, val lon: Float)
