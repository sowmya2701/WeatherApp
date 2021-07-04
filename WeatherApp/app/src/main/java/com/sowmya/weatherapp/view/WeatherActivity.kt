package com.sowmya.weatherapp.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.sowmya.weatherapp.R
import com.sowmya.weatherapp.db.WeatherDatabase
import com.sowmya.weatherapp.model.WeatherResponse
import com.sowmya.weatherapp.network.WeatherNetworkRequest
import com.sowmya.weatherapp.presenter.IWeatherInterface
import com.sowmya.weatherapp.presenter.WeatherInfoImpl
import com.sowmya.weatherapp.util.Utils
import java.util.*

/*
  This is the Main Activity to display the WeatherInfo
 */
class WeatherActivity : Activity(), IWeatherInterface.updateActivity{

    var weatherDataTV: TextView? = null
    private var networkRequest: WeatherNetworkRequest = WeatherNetworkRequest()
    val TAG: String = "MainActivity"
    //This is a database reference to to store and retrieve weather information offline
    lateinit var weatherInfoImpl: WeatherInfoImpl
    lateinit var utilObject: Utils
    lateinit var weatherDB: WeatherDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate")
        weatherDataTV = findViewById(com.sowmya.weatherapp.R.id.textView)
        getLatLongValues()
        weatherInfoImpl = WeatherInfoImpl(this)
        weatherDB = WeatherDatabase(this)
        utilObject = Utils()
        //OnButton Click fetchWeatherDetails will fetch weather information of current location
        findViewById<View>(R.id.button).setOnClickListener {
            //Check if device connected to internet is through WIFI
            if (utilObject.isOnline(this)) {
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        fetchWeatherDetails()
                    }
                }, 2000, 120000)
            } else {
                //Device not connected to internet through wifi hence fetch data from database
                fetchWeatherDetailsFromDB()
            }
        }
    }

    /*
     *fetchWeatherDetailsFromDB to read weatherdata from database
    */
    private fun fetchWeatherDetailsFromDB() {
        var responseList: HashMap<String, String> = weatherDB.readAllWeatherData()
        updateTextViewOffline(responseList)
        Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show()
    }

    /*
      *fetchWeatherDetails will check if the device internet is connected through WIFI, if yes then it tries to fetch the data
     */
    fun fetchWeatherDetails() {
        weatherInfoImpl.fetchWeatherDetails()
    }

    /*
     *This method is to update the textView in UIThread,
     *response recived from database
     */
    override fun updateTextViewOffline(weatherInfo : HashMap<String, String>) {
        weatherDataTV!!.text = weatherInfo.get("temp") + weatherInfo.get("humidity")
    }

    /*
     This method is to update the textView in UIThread, response recived from server
     */
    override fun updateTextView(
        weatherResponse: WeatherResponse
    ) {
        Log.d(TAG, "updating TextView")
        val inserted: Long = weatherDB.insertWeatherData(weatherResponse)
        Log.d(TAG, "is inserted database: " + inserted)
        weatherDataTV!!.text =
            weatherResponse.main!!.temp.toString() + weatherResponse.main!!.humidity.toString() + weatherResponse.sys!!.country.toString()
    }

    fun getLatLongValues() {
        var lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val location = lm!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            WeatherNetworkRequest.lon = location!!.getLongitude()
            WeatherNetworkRequest.lat = location!!.getLatitude()
            return
        }
    }
}
