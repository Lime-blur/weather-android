package ru.limedev.weather.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.limedev.weather.data.model.WeatherDto

interface WeatherDao {

    @GET("data/2.5/onecall")
    suspend fun requestWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("exclude") exclude: String,
        @Query("appid") apiKey: String
    ): Response<WeatherDto>
}