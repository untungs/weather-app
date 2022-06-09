package com.untungs.weatherapp.network.model

import kotlinx.serialization.Serializable

@Serializable
data class CityGeo(
    val name: String,
    val lat: Float,
    val lon: Float,
    val country: String,
    val state: String? = null
)