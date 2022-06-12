package com.untungs.weatherapp.data.repository

import com.untungs.weatherapp.common.toDateString
import com.untungs.weatherapp.data.Temp
import com.untungs.weatherapp.data.Weather
import com.untungs.weatherapp.data.WeatherDailyStat
import com.untungs.weatherapp.data.WeatherStat
import com.untungs.weatherapp.local.dao.FavoriteLocationDao
import com.untungs.weatherapp.local.entity.CurrentWeatherDb
import com.untungs.weatherapp.local.entity.WeatherDb
import com.untungs.weatherapp.network.NetworkDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AppWeatherRepository @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val networkDataSource: NetworkDataSource,
    private val favoriteLocationDao: FavoriteLocationDao
) : WeatherRepository {
    override suspend fun getWeatherDaily(lat: Float, lon: Float): WeatherDailyStat =
        withContext(dispatcher) {
            networkDataSource.getWeatherDaily(lat, lon).let {
                val current = with(it.current) {
                    val currentWeather = with(weather.first()) {
                        Weather(main, description, icon)
                    }
                    WeatherStat.CurrentWeatherStat(
                        "Current", dt.times(1000), currentWeather, humidity, wind_speed, temp
                    )
                }
                val daily = it.daily.mapIndexed { index, daily ->
                    val dailyWeather = with(daily.weather.first()) {
                        Weather(main, description, icon)
                    }
                    val temp = Temp(daily.temp.day, daily.temp.night)
                    var day = daily.dt.times(1000).toDateString()
                    if (index == 0) {
                        day = day.replaceBefore(",", "Today")
                    }
                    WeatherStat.DailyWeatherStat(
                        day,
                        daily.dt.times(1000),
                        dailyWeather,
                        daily.humidity,
                        daily.wind_speed,
                        temp
                    )
                }
                WeatherDailyStat(current, daily)
            }
        }

    override suspend fun updateWeather(lat: Float, lon: Float) = withContext(dispatcher) {
        val location = favoriteLocationDao.getFavoriteLocation(lat, lon) ?: return@withContext
        val weatherDaily = networkDataSource.getWeatherDaily(lat, lon)
        val updatedWeather = with(weatherDaily.current) {
            val weather1 = weather.first()
            location.copy(
                currentWeather = CurrentWeatherDb(
                    WeatherDb(weather1.main, weather1.description, weather1.icon),
                    humidity,
                    wind_speed,
                    temp,
                    dt.times(1000)
                )
            )
        }
        favoriteLocationDao.update(updatedWeather)
    }
}