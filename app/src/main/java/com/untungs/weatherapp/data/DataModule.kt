package com.untungs.weatherapp.data

import com.untungs.weatherapp.data.repository.AppLocationRepository
import com.untungs.weatherapp.data.repository.AppWeatherRepository
import com.untungs.weatherapp.data.repository.LocationRepository
import com.untungs.weatherapp.data.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindsLocationRepository(
        locationRepository: AppLocationRepository
    ): LocationRepository

    @Binds
    fun bindsWeatherRepository(
        weatherRepository: AppWeatherRepository
    ): WeatherRepository
}