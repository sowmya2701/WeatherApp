package com.sowmya.weatherapp.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.sowmya.weatherapp.model.WeatherResponse

/*
  Database class to support insert,delete,update of Weather details
 */
class WeatherDatabase(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    @Throws(SQLiteConstraintException::class)
    fun insertWeatherData(weatherInfo: WeatherResponse): Long {
        // Gets the data repository in write mode
        val db = writableDatabase

        // Create a new map of values, where column names are the keys
        val values = ContentValues()
        values.put(DBContract.WeatherEntry.COLUMN_TEMP, weatherInfo.main!!.temp)
        values.put(DBContract.WeatherEntry.COLUMN_HUMIDITY, weatherInfo.main!!.humidity)

        // Insert the new row, returning the primary key value of the new row
        val newRowId = db.insert(DBContract.WeatherEntry.TABLE_NAME, null, values)
        return newRowId
    }

    @Throws(SQLiteConstraintException::class)
    fun deleteWeatherData(id: String): Boolean {
        val db = writableDatabase
        val selection = DBContract.WeatherEntry.COLUMN_ID + " LIKE ?"
        val selectionArgs = arrayOf(id)
        db.delete(DBContract.WeatherEntry.TABLE_NAME, selection, selectionArgs)

        return true
    }

    fun readAllWeatherData(): HashMap<String, String> {
        val weatherInfo = HashMap<String, String>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + DBContract.WeatherEntry.TABLE_NAME, null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return HashMap()
        }

        var temperature: String
        var humidity: String
        if (cursor!!.moveToFirst()) {
            while (!cursor.isAfterLast) {
                temperature = cursor.getString(cursor.getColumnIndex(DBContract.WeatherEntry.COLUMN_TEMP))
                humidity = cursor.getString(cursor.getColumnIndex(DBContract.WeatherEntry.COLUMN_HUMIDITY))
                weatherInfo.put("temp", temperature)
                weatherInfo.put("humidity", humidity)
                cursor.moveToNext()
            }
        }
        return weatherInfo
    }

    companion object {
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "Weather.db"

        private val SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DBContract.WeatherEntry.TABLE_NAME + " (" +
                    DBContract.WeatherEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DBContract.WeatherEntry.COLUMN_TEMP + " TEXT," +
                    DBContract.WeatherEntry.COLUMN_HUMIDITY + " TEXT)"

        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DBContract.WeatherEntry.TABLE_NAME
    }
}