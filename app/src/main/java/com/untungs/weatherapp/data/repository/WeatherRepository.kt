package com.untungs.weatherapp.data.repository

import com.untungs.weatherapp.data.WeatherDailyStat

interface WeatherRepository {
    suspend fun getWeatherDaily(lat: Float, lon: Float): WeatherDailyStat

    suspend fun updateWeather(lat: Float, lon: Float)
}