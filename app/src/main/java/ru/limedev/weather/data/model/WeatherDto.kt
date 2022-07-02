package ru.limedev.weather.data.model

data class WeatherDto(
    val current: Weather?,
    val daily: List<Weather>?
) {

    data class Weather(
        val dt: String?,
        val weather: List<WeatherDescription>?
    )

    data class WeatherDescription(
        val main: String?
    )
}

