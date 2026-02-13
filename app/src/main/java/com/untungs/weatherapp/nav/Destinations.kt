package com.untungs.weatherapp.nav

import kotlinx.serialization.Serializable

@Serializable object Home

@Serializable data class Search(val q: String = "")

@Serializable data class WeatherDaily(val city: String, val lat: Float, val lon: Float)
