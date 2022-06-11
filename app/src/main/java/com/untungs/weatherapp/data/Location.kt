package com.untungs.weatherapp.data

data class Location(
    val name: String,
    val lat: Float,
    val lon: Float
)

data class LocationWithCurrentWeather(
    val location: Location,
    val weather: WeatherStat.CurrentWeatherStat
)