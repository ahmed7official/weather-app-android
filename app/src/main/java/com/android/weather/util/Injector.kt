package com.android.weather.util

import com.android.weather.data.remote.HeaderInterceptor
import com.android.weather.data.remote.WebService
import com.android.weather.data.repositories.HomeRepository
import com.android.weather.ui.home.HomeViewModelFactory

object Injector {

    fun getHomeViewModelFactory(): HomeViewModelFactory{

        val interceptor = HeaderInterceptor()
        val webService = WebService.invoke(interceptor)

        val repo = HomeRepository(webService)
        return HomeViewModelFactory(repo)

    }//getHomeViewModelFactory

}