package ru.limedev.weather.data.model

data class WeatherDbDto(
    val currentWeather: WeatherDbInfo,
    val dailyWeather: List<WeatherDbInfo>
) {

    data class WeatherDbInfo(
        val description: String?
    )
}