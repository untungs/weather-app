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
    val day: String
    val weather: Weather
    val humidity: Float
    val windSpeed: Float

    data class CurrentWeatherStat(
        override val day: String,
        override val weather: Weather,
        override val humidity: Float,
        override val windSpeed: Float,
        val temp: Float
    ) : WeatherStat

    data class DailyWeatherStat(
        override val day: String,
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

val FAKE_CITY = CityWeather(
    City("Yogyakarta", "Special Region of Yogyakarta", "ID", -7.8012f, 110.364917f),
    WeatherStat.CurrentWeatherStat(
        "Today", Weather("Clouds", "overcast clouds", "04n"), 24.5f, 88f, 1.68f
    )
)