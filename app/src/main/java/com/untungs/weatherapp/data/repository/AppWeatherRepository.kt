package com.untungs.weatherapp.data.repository

import com.untungs.weatherapp.common.toDateString
import com.untungs.weatherapp.data.Weather
import com.untungs.weatherapp.data.WeatherDailyStat
import com.untungs.weatherapp.data.WeatherStat
import com.untungs.weatherapp.local.dao.FavoriteLocationDao
import com.untungs.weatherapp.local.entity.CurrentWeatherDb
import com.untungs.weatherapp.local.entity.WeatherDb
import com.untungs.weatherapp.network.NetworkDataSource
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class AppWeatherRepository @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val networkDataSource: NetworkDataSource,
    private val favoriteLocationDao: FavoriteLocationDao
) : WeatherRepository {
    override suspend fun getWeatherDaily(lat: Float, lon: Float): WeatherDailyStat =
        withContext(dispatcher) {
            val currentWeather = networkDataSource.getCurrentWeather(lat, lon)
            val forecast = networkDataSource.getForecast(lat, lon)

            val current = with(currentWeather) {
                val weather1 = with(weather.first()) { Weather(main, description, icon) }
                WeatherStat.CurrentWeatherStat(
                    "Current",
                    dt.times(1000),
                    weather1,
                    main.humidity,
                    wind.speed,
                    main.temp
                )
            }

            val daily = forecast.list
                .groupBy { it.dt.times(1000).toDateString().substringBefore(",") }
                .map { (dayName, forecasts) ->
                    // Pick the middle one for the day, or just the one with max
                    // temp
                    val representative =
                        forecasts.maxByOrNull { it.main.temp }
                            ?: forecasts.first()
                    val dailyWeather =
                        with(representative.weather.first()) {
                            Weather(main, description, icon)
                        }
                    val timestamp = representative.dt.times(1000)
                    var day = timestamp.toDateString()

                    WeatherStat.DailyWeatherStat(
                        day,
                        timestamp,
                        dailyWeather,
                        representative.main.humidity,
                        representative.wind.speed,
                        representative.main.temp
                    )
                }
                .mapIndexed { index, dailyWeatherStat ->
                    if (index == 0) {
                        dailyWeatherStat.copy(
                            day = dailyWeatherStat.day.replaceBefore(",", "Today")
                        )
                    } else {
                        dailyWeatherStat
                    }
                }

            WeatherDailyStat(current, daily)
        }

    override suspend fun updateWeather(lat: Float, lon: Float) = withContext(dispatcher) {
        val location = favoriteLocationDao.getFavoriteLocation(lat, lon) ?: return@withContext
        val currentWeather = networkDataSource.getCurrentWeather(lat, lon)
        val updatedWeather = with(currentWeather) {
            val weather1 = weather.first()
            location.copy(
                currentWeather = CurrentWeatherDb(
                    WeatherDb(weather1.main, weather1.description, weather1.icon),
                    main.humidity,
                    wind.speed,
                    main.temp,
                    dt.times(1000)
                )
            )
        }
        favoriteLocationDao.update(updatedWeather)
    }
}
