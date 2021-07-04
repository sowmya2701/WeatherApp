package com.sowmya.weatherapp.presenter

import android.util.Log
import com.sowmya.weatherapp.model.WeatherResponse
import com.sowmya.weatherapp.network.WeatherNetworkRequest

open class WeatherInfoImpl(var weatherListner: IWeatherInterface.updateActivity) : IWeatherInterface.fetchWeatherDetails{
    var TAG: String = "WeatherInfoImpl"
    private var networkRequest: WeatherNetworkRequest = WeatherNetworkRequest()

    override fun fetchWeatherDetails() {
        Log.d(TAG, "fetchWeatherDetails")
        networkRequest.getCurrentWeatherData(this)
    }

    fun sendResponseData(weatherResponse: WeatherResponse) {
        Log.d(TAG, "sendResponseData")
        weatherListner.updateTextView(weatherResponse)
    }
}