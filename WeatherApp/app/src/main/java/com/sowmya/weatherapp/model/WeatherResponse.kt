package com.sowmya.weatherapp.model

import com.google.gson.annotations.SerializedName

/*
Pojo class to define the fields similar to Json response fields
 */
open class WeatherResponse {

    var id: Int? = null

    //Here @SerializedName annotation relates the string defined in the class to the field from json response
    @SerializedName("sys")
    var sys: Sys? = null

    @SerializedName("main")
    var main: Main? = null
    
}

class Main {
    @SerializedName("temp")
    var temp: String = "0.0"
    @SerializedName("humidity")
    var humidity: String = "0.0"
    @SerializedName("pressure")
    var pressure: String = "0.0"
    @SerializedName("temp_min")
    var temp_min: String = "0.0"
    @SerializedName("temp_max")
    var temp_max: String = "0.0"
}

class Sys {
    @SerializedName("country")
    var country: String? = null
}