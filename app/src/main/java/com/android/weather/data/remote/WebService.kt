package com.android.weather.data.remote

import com.android.weather.BuildConfig
import com.android.weather.data.models.ApiDataResponse
import com.android.weather.data.models.wheather.WeatherData
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

@JvmSuppressWildcards
interface WebService {


    @GET("weather")
    suspend fun getWeatherDataByCityName(
        @Query("q") cityName: String,
        @Query("appid") appid: String = BuildConfig.OPEN_WHEATHER_API_KEY,
    ): Response<WeatherData>


    @GET("weather")
    suspend fun getWeatherDataByLatLng(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String = BuildConfig.OPEN_WHEATHER_API_KEY,
    ): Response<WeatherData>


    companion object {


        operator fun invoke(headerInterceptor: HeaderInterceptor): WebService {

            val okHttpClint = OkHttpClient.Builder()
                .readTimeout(1, TimeUnit.MINUTES)
                .addInterceptor(headerInterceptor)

                .addInterceptor(HttpLoggingInterceptor().apply {
                    level =
                        if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
                })

                .build()

            return Retrofit.Builder()
                .client(okHttpClint)
                .baseUrl(BuildConfig.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WebService::class.java)
        }//invoke()
    }//companion object

}//WebService

