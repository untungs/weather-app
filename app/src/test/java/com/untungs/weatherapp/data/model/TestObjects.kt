package com.untungs.weatherapp.data.model

import com.untungs.weatherapp.data.Weather
import com.untungs.weatherapp.data.WeatherDailyStat
import com.untungs.weatherapp.data.WeatherStat


val testWeather = Weather("", "", "")
val testCurrentWeather = WeatherStat.CurrentWeatherStat(
    "Today", 0, testWeather, 30.57f, 10.08f, 30f
)

val testWeatherDailyStat = WeatherDailyStat(testCurrentWeather, emptyList())