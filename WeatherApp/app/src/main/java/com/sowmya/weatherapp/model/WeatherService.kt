package com.sowmya.weatherapp.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/*
  WeatherService class helps to reach the server API using annotations ex @GET
 */
interface WeatherService {
    @GET("data/2.5/weather?")
    fun getCurrentWeatherData(@Query("lat") lat: Double, @Query("lon") lon: Double, @Query("APPID") app_id: String): Call<WeatherResponse>
}

