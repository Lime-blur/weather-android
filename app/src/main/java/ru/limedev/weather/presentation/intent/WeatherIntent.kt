package ru.limedev.weather.presentation.intent

import ru.limedev.weather.domain.entity.WeatherRequestEntity

sealed class WeatherIntent {
    data class FetchDailyWeather(
        val weatherRequestEntity: WeatherRequestEntity
    ) : WeatherIntent()
    object FetchLastSelectedCityType: WeatherIntent()
}