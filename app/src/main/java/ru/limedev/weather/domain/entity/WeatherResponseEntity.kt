package ru.limedev.weather.domain.entity

import ru.limedev.weather.data.model.WeatherDto

data class WeatherResponseEntity(
    val cityType: CityType,
    val weather: WeatherDto?
)