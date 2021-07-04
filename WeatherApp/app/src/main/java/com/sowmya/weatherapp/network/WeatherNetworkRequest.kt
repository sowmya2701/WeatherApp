package com.sowmya.weatherapp.network

import android.util.Log
import com.sowmya.weatherapp.model.WeatherResponse
import com.sowmya.weatherapp.model.WeatherService
import com.sowmya.weatherapp.presenter.WeatherInfoImpl
import com.sowmya.weatherapp.util.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/*
This class helps to communicate with the server OpenWeatherAPI to fetch weather details for the given latitude and logitude values
 */
open class WeatherNetworkRequest() {

    val TAG: String = "NetworkRequest"

    fun getCurrentWeatherData(weatherInfoImpl: WeatherInfoImpl) {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(WeatherService::class.java)
        val call = service.getCurrentWeatherData(lat, lon, Constants.AppId)

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if (response.code() == Constants.responseCode) {
                    val weatherResponse = response.body()!!
                    Log.d(TAG, "response body" + response.body()!!)
                    weatherInfoImpl.sendResponseData(weatherResponse)
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.d(TAG, "onFailure" + "Connection Failed")
            }
        })
    }

    /*
      Here in the companion object all fields are converted to static fields
     */
    companion object {
        var lat: Double = 0.0
        var lon: Double = 0.0
    }
}