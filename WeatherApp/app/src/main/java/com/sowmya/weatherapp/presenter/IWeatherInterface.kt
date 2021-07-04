package com.sowmya.weatherapp.presenter

import com.sowmya.weatherapp.model.WeatherResponse

/*
  IWeatherInterface helps to communicate between MainActivity and Network Request
 */
interface IWeatherInterface {

    interface fetchWeatherDetails {
          fun fetchWeatherDetails()
    }

    interface updateActivity {
        fun updateTextView(weatherResponse: WeatherResponse)
        fun updateTextViewOffline(weatherInfo : HashMap<String, String>)
    }
}