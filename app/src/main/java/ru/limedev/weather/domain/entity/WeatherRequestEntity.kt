package ru.limedev.weather.domain.entity

data class WeatherRequestEntity(
    val cityType: CityType,
    val exclude: String = "minutely,hourly,alerts",
    val apiKey: String
)