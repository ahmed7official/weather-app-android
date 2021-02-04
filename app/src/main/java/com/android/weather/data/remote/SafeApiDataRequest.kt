package com.android.weather.data.remote

import com.android.weather.data.models.ApiDataResponse
import org.json.JSONObject
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException



@Suppress("UNCHECKED_CAST")
abstract class SafeApiDataRequest {

    companion object {

        const val ERROR_NO_INTERNET_CONNECTION = "no_internet_connection"
        const val ERROR_TIMEOUT = "timeout"


    }

    /**
     * @param call is a suspend fun with no params and return -> Response<T> (retrofit2.Response)
     *     this fun check if the response isSuccessful or not and handel the exception
     */
    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): ApiDataResponse<T> {

        try {

            val response = call.invoke()

            if (response.isSuccessful) {


                return ApiDataResponse(
                    response.isSuccessful, response.code(),
                    response.message(),
                    response.body()
                )

            } else {

                return ApiDataResponse(response.isSuccessful, response.code(), response.message(), null)

            }

        } catch (e: Exception) {
            e.printStackTrace()

            return when (e) {

                is UnknownHostException -> {
                    ApiDataResponse(false, 0, ERROR_NO_INTERNET_CONNECTION, null)
                }

                is SocketTimeoutException -> {
                    ApiDataResponse(false, 0, ERROR_TIMEOUT, null)
                }

                else -> {
                    ApiDataResponse(false, 0, e.message.toString(), null)
                }

            }//when

        }//catch

    }//apiRequest()

}//SafeApiRequest