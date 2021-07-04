package com.sowmya.weatherapp.db

import android.provider.BaseColumns

object DBContract {
    /* Inner class that defines the table contents */
    class WeatherEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "weatherInfo"
            val COLUMN_ID = "id"
            val COLUMN_TEMP = "temp"
            val COLUMN_HUMIDITY = "humi"
        }
    }
}