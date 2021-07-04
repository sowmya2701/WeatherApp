package com.sowmya.weatherapp

import android.net.NetworkRequest
import com.sowmya.weatherapp.model.WeatherResponse
import com.sowmya.weatherapp.network.WeatherNetworkRequest
import com.sowmya.weatherapp.presenter.WeatherInfoImpl
import junit.framework.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`

class WeatherInfoTest {

    @Test
    fun testsWork() {
        assertTrue(true)
    }

    @Test
    fun checkWeatherInfoFromServer() {
        val networkInfo = Mockito.mock(WeatherNetworkRequest::class.java)
        val weatherInfoImpl = Mockito.mock(WeatherInfoImpl::class.java)
        val weatherResponseData = Mockito.mock(WeatherResponse::class.java)
        `when`(networkInfo.getCurrentWeatherData(weatherInfoImpl)).then { weatherInfoImpl.sendResponseData(weatherResponseData)}
    }
}