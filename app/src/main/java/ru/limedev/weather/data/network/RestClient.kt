package ru.limedev.weather.data.network

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val URL_TAG = "Weather Base URL"

private const val BASE_URL = "https://api.openweathermap.org/data/2.5/onecall"

object RestClient {

    private var retrofit: Retrofit? = null

    val weatherDao: WeatherDao?
        get() = getClient()?.create(WeatherDao::class.java)

    private fun getClient(): Retrofit? {
        Log.i(URL_TAG, BASE_URL)
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit
    }
}