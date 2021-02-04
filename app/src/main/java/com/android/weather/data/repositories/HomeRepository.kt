package com.android.weather.data.repositories

import com.android.weather.data.models.ApiDataResponse
import com.android.weather.data.models.wheather.WeatherData
import com.android.weather.data.remote.SafeApiDataRequest
import com.android.weather.data.remote.WebService

class HomeRepository(private val webService: WebService) : SafeApiDataRequest() {


    suspend fun getWeatherDataByCityName(cityName: String): ApiDataResponse<WeatherData> {
        return apiRequest { webService.getWeatherDataByCityName(cityName) }
    }

    suspend fun getWeatherDataByLatLng(lat: Double, lon: Double): ApiDataResponse<WeatherData> {
        return apiRequest { webService.getWeatherDataByLatLng(lat, lon) }
    }

}