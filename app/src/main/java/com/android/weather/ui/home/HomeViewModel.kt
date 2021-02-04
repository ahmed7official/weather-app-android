package com.android.weather.ui.home

import android.location.Location
import androidx.lifecycle.ViewModel
import com.android.weather.data.models.ApiDataResponse
import com.android.weather.data.models.wheather.WeatherData
import com.android.weather.data.repositories.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HomeViewModel(private val repository: HomeRepository) : ViewModel() {

    var lastLocation: Location? = null

    fun getWeatherData(cityName: String): Flow<ApiDataResponse<WeatherData>> {

        return flow {

            emit(repository.getWeatherDataByCityName(cityName))

        }

    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    fun getWeatherData(lat: Double, lon: Double): Flow<ApiDataResponse<WeatherData>> {

        return flow {

            emit(repository.getWeatherDataByLatLng(lat, lon))

        }

    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

}