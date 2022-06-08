package com.untungs.weatherapp.data

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

data class WeatherStat(
    val weather: Weather,
    val temp: Float,
    val humidity: Float,
    val windSpeed: Float
)

data class Weather(
    val main: String,
    val description: String,
    val icon: String
)

val FAKE_CITY = CityWeather(
    City("Yogyakarta", "Special Region of Yogyakarta", "ID", -7.8012f, 110.364917f),
    WeatherStat(
        Weather("Clouds", "overcast clouds", "04n"), 24.5f, 88f, 1.68f
    )
)