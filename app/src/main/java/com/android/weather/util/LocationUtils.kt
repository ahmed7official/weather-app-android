package com.android.weather.util

import android.app.Activity
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest

fun displayLocationSettingsRequest(activity: Activity, requestCode: Int = 6255) {

    val locationRequest = LocationRequest.create()
    locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

    val interval: Long = 10000

    locationRequest.interval = interval
    locationRequest.fastestInterval = interval / 2


    val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
    builder.setAlwaysShow(true)


    val result = LocationServices.getSettingsClient(activity)
        .checkLocationSettings(builder.build())


    result.addOnFailureListener { e ->

        if (e is ResolvableApiException) {

            try {
                // Handle result in onActivityResult()
                e.startResolutionForResult(activity, requestCode)

            } catch (e: Exception) {

                e.printStackTrace()

            }
        }


    }

}
