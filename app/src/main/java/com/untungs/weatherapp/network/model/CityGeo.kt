package com.untungs.weatherapp.network.model

import com.untungs.weatherapp.data.CityLocation
import kotlinx.serialization.Serializable

@Serializable
data class CityGeo(
    val name: String,
    val lat: Float,
    val lon: Float,
    val country: String,
    val state: String? = null
)

fun CityGeo.toDomainModel() = CityLocation(
    cityName = "$name${state?.let { ", $it" } ?: ""}, $country",
    lat = lat,
    lon = lon
)
