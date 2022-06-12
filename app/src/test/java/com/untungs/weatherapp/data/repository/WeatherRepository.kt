package com.untungs.weatherapp.data.repository

import com.untungs.weatherapp.data.WeatherDailyStat
import com.untungs.weatherapp.data.model.testWeatherDailyStat

class TestWeatherRepository : WeatherRepository {
    override suspend fun getWeatherDaily(lat: Float, lon: Float): WeatherDailyStat {
        return testWeatherDailyStat
    }

    override suspend fun updateWeather(lat: Float, lon: Float) {

    }
}