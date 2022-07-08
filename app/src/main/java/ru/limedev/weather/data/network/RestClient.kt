package ru.limedev.weather.data.network

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val URL_TAG = "Weather Base URL"

private const val BASE_URL = "https://api.openweathermap.org/"

object RestClient {

    val weatherDao: WeatherDao?
        get() = getClient()?.create(WeatherDao::class.java)

    private var retrofit: Retrofit? = null

    private fun getClient(): Retrofit? {
        Log.i(URL_TAG, BASE_URL)
        if (retrofit == null) {
            val client = buildClient()
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit
    }

    private fun buildClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.apply { interceptor.level = HttpLoggingInterceptor.Level.BASIC }
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }
}