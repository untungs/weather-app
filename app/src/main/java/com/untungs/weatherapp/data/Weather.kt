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

data class WeatherDailyStat(
    val current: WeatherStat.CurrentWeatherStat,
    val daily: List<WeatherStat.DailyWeatherStat>
)

sealed interface WeatherStat {
    val dayOrLocation: String
    val timestamp: Long
    val weather: Weather
    val humidity: Float
    val windSpeed: Float

    data class CurrentWeatherStat(
        override val dayOrLocation: String,
        override val timestamp: Long,
        override val weather: Weather,
        override val humidity: Float,
        override val windSpeed: Float,
        val temp: Float
    ) : WeatherStat

    data class DailyWeatherStat(
        override val dayOrLocation: String,
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