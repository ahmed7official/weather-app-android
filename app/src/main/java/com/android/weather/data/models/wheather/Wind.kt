package com.android.weather.data.models.wheather


import com.google.gson.annotations.SerializedName

data class Wind(
    @SerializedName("speed")
    val speed: Double? = null,
    @SerializedName("deg")
    val deg: Int? = null
)