package com.untungs.weatherapp.network

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface NetworkModule {

    @Binds
    fun bindsNiaNetwork(retrofitNetwork: RetrofitNetwork): NetworkDataSource

}
