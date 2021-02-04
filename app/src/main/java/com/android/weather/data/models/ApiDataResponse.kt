package com.android.weather.data.models

data class ApiDataResponse<ResponseType>(

    val success: Boolean,
    val statusCode: Int?,
    val statusMessage: String?,
    val data: ResponseType?
) {


    /**
     * Performs the given [action] if success member variable is true && data member variable is
     * not null.
     * Returns the original `ApiResponse` unchanged.
     */
    fun onSuccess(action: (data: ResponseType) -> Unit): ApiDataResponse<ResponseType> {

        if (success && data != null) action(data)

        return this

    }//onSuccess()


    /**
     * Performs the given [action] if success member variable is false or data member variable is
     *  null.
     * Returns the original `ApiResponse` unchanged.
     */
    fun onFailure(action: (message: String, code: Int) -> Unit): ApiDataResponse<ResponseType> {

        if (!success || data == null) action(statusMessage!!, statusCode!!)

        return this

    }//onFailure()
}