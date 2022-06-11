package com.untungs.weatherapp.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.untungs.weatherapp.BuildConfig
import com.untungs.weatherapp.network.model.CityGeo
import com.untungs.weatherapp.network.model.WeatherDaily
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

private interface NetworkApi {

    @GET(value = "geo/1.0/direct")
    suspend fun getCities(
        @Query("q") name: String,
        @Query("limit") limit: Int = 10
    ): List<CityGeo>

    @GET(value = "data/2.5/onecall")
    suspend fun getWeatherDaily(
        @Query("lat") lat: Float,
        @Query("lon") lon: Float,
        @Query("units") units: String = "metric",
        @Query("exclude") exclude: String = "hourly,minutely"
    ): WeatherDaily
}

@Singleton
class RetrofitNetwork @Inject constructor() : NetworkDataSource {

    private val json = Json { ignoreUnknownKeys = true }

    @OptIn(ExperimentalSerializationApi::class)
    private val networkApi = Retrofit.Builder()
        .baseUrl(BuildConfig.API_URL)
        .client(
            OkHttpClient.Builder()
                .addInterceptor(NetworkInterceptor())
                .addInterceptor(
                    HttpLoggingInterceptor().apply {
                        setLevel(HttpLoggingInterceptor.Level.BODY)
                    }
                )
                .build()
        )
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(NetworkApi::class.java)

    override suspend fun getCities(name: String): List<CityGeo> = networkApi.getCities(name)

    override suspend fun getWeatherDaily(lat: Float, lon: Float): WeatherDaily =
        networkApi.getWeatherDaily(lat, lon)
}

class NetworkInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = with(chain.request()) {
            val url = url.newBuilder()
                .addQueryParameter("appid", "c52ce88317a16b2e83391b2a47e86a01")
                .build()

            newBuilder().url(url).build()
        }

        return chain.proceed(request)
    }
}