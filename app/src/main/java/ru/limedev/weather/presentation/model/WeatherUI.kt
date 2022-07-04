package ru.limedev.weather.presentation.model

import ru.limedev.weather.domain.entity.CityType
import java.util.*

data class WeatherUI(
    val cityType: CityType,
    val currentWeather: WeatherUIInfo,
    val dailyWeather: List<WeatherUIInfo>
) {

    data class WeatherUIInfo(
        val date: Calendar?,
        val weatherList: List<WeatherUIDescription>
    )

    data class WeatherUIDescription(
        val description: String
    )
}