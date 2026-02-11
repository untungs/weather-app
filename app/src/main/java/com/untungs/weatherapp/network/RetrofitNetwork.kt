package com.untungs.weatherapp.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.untungs.weatherapp.BuildConfig
import com.untungs.weatherapp.common.NO_CONNECTION
import com.untungs.weatherapp.common.SOMETHING_WENT_WRONG
import com.untungs.weatherapp.network.model.CityGeo
import com.untungs.weatherapp.network.model.NetworkForecast
import com.untungs.weatherapp.network.model.NetworkWeather
import java.io.IOException
import java.lang.Exception
import java.net.ConnectException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton
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

private interface NetworkApi {

    @GET(value = "geo/1.0/direct")
    suspend fun getCities(
        @Query("q") name: String,
        @Query("limit") limit: Int = 10
    ): List<CityGeo>

    @GET(value = "data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Float,
        @Query("lon") lon: Float,
        @Query("units") units: String = "metric"
    ): NetworkWeather

    @GET(value = "data/2.5/forecast")
    suspend fun getForecast(
        @Query("lat") lat: Float,
        @Query("lon") lon: Float,
        @Query("units") units: String = "metric"
    ): NetworkForecast
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

    override suspend fun getCurrentWeather(lat: Float, lon: Float): NetworkWeather =
        networkApi.getCurrentWeather(lat, lon)

    override suspend fun getForecast(lat: Float, lon: Float): NetworkForecast =
        networkApi.getForecast(lat, lon)
}

class NetworkInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = with(chain.request()) {
            val url = url.newBuilder()
                .addQueryParameter("appid", BuildConfig.API_KEY)
                .build()

            newBuilder().url(url).build()
        }

        val response = try {
            chain.proceed(request)
        } catch (error: Exception) {
            throw when (error) {
                is UnknownHostException, is ConnectException -> {
                    IOException(NO_CONNECTION)
                }

                else -> IOException(SOMETHING_WENT_WRONG)
            }
        }
        return response
    }
}
