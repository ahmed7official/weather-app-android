package com.android.weather.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.android.weather.R
import com.android.weather.data.models.ApiDataResponse
import com.android.weather.data.models.wheather.WeatherData
import com.android.weather.databinding.ActivityHomeBinding
import com.android.weather.util.*
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.collect


private const val TAG = "tag_home"

class HomeActivity : AppCompatActivity() {

    private lateinit var viewModel: HomeViewModel

    private lateinit var binding: ActivityHomeBinding

    private val locationManager by lazy { getSystemService(Context.LOCATION_SERVICE) as LocationManager }

    private val fusedLocationProvider by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }//fusedLocationProvider

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        initViewModel()
        initListeners()


        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            displayLocationSettingsRequest(this)

        }

    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private fun initListeners() {

        binding.btnSearch.setOnClickListener(btnSearchClickListener)

    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private val btnSearchClickListener = View.OnClickListener {

        if (binding.inputCity.text.isNullOrBlank()) {

            showToast(R.string.err_city_req)

        } else {

            binding.inputCity.hideSoftKeyboard()

            showLoading()

            val cityName = binding.inputCity.text.toString().trim()

            lifecycleScope.launchWhenCreated {

                viewModel.getWeatherData(cityName).collect {

                    onResponse.invoke(it)

                }

            }

        }

    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private val onResponse = { response: ApiDataResponse<WeatherData> ->

        response.onSuccess {

            Log.i(TAG, "$it")

            displayResults(it)

            hideLoading()

        }

        response.onFailure { message, code ->

            Log.e(TAG, message)

            hideLoading()

            when (code) {

                404 -> showToast(R.string.city_not_found)

                else -> showToast(R.string.something_went_wrong)

            }

        }

    }//onResponse

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private fun showLoading() = binding.layoutLoading.show()

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private fun hideLoading() = binding.layoutLoading.hide()

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private fun displayResults(weatherData: WeatherData) {

        if (!weatherData.weather.isNullOrEmpty()) {

            binding.tvWeather.text = weatherData.weather[0].description

        }

        binding.tvTemp.text = weatherData.main?.temp.toString()

        binding.tvHumidity.text = weatherData.main?.humidity.toString()

        binding.tvPressure.text = weatherData.main?.pressure.toString()

        binding.tvWindSpeed.text = weatherData.wind?.speed.toString()

        binding.inputCity.setText(weatherData.name.toString())

        binding.cardResults.show()

    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @SuppressLint("MissingPermission")
    private fun fetchDataToCurrentLocation() {

            lifecycleScope.launchWhenCreated {

                viewModel.getWeatherData(
                    viewModel.lastLocation!!.latitude,
                    viewModel.lastLocation!!.latitude
                ).collect {

                    onResponse.invoke(it)

                }

            }

    }//fetchDataToCurrentLocation()

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    override fun onStart() {
        super.onStart()

        startLocationUpdates()

    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates(){

        val locationRequest = LocationRequest.create().apply {
            interval = 5000
            fastestInterval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        fusedLocationProvider.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )

    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    override fun onStop() {
        super.onStop()

        removeLocationUpdates()

    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private val locationCallback = object : LocationCallback() {

        override fun onLocationResult(result: LocationResult?) {
            super.onLocationResult(result)

            result?.lastLocation?.apply {

                viewModel.lastLocation = this

                hideLoading()
                fetchDataToCurrentLocation()
                removeLocationUpdates()

            }//apply

        }//onLocationResult()

    }//locationCallback

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private fun removeLocationUpdates(){

        fusedLocationProvider.removeLocationUpdates(locationCallback)

    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private fun initViewModel() {

        viewModel = ViewModelProvider(
            this,
            Injector.getHomeViewModelFactory()
        ).get(HomeViewModel::class.java)

    }

}