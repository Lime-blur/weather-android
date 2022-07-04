package ru.limedev.weather.domain.mappers

import ru.limedev.weather.data.model.WeatherDto
import ru.limedev.weather.domain.WeatherResult
import ru.limedev.weather.domain.entity.WeatherResponseEntity
import ru.limedev.weather.domain.utilities.emptyString
import ru.limedev.weather.domain.utilities.millisToLong
import ru.limedev.weather.presentation.model.WeatherUI
import ru.limedev.weather.presentation.viewstate.WeatherState
import java.util.*

fun <T> WeatherResult<T>.toWeatherViewState(): WeatherState {
    return when(this) {
        is WeatherResult.Success<T> -> {
            val weatherResponseEntity = value as? WeatherResponseEntity
            WeatherState.Success(weatherResponseEntity?.toWeatherUI())
        }
        is WeatherResult.Failure<T> -> WeatherState.Error(error)
    }
}

fun WeatherResponseEntity.toWeatherUI(): WeatherUI {
    return WeatherUI(
        cityType = cityType,
        currentWeather = weather?.current.toWeatherUIInfo(),
        dailyWeather = weather?.daily?.map { it.toWeatherUIInfo() } ?: listOf()
    )
}

fun WeatherDto.Weather?.toWeatherUIInfo(): WeatherUI.WeatherUIInfo {
    return WeatherUI.WeatherUIInfo(
        date = convertStringToDate(this?.dt),
        weatherList = this?.weather?.map { it.toWeatherUIDescription() } ?: listOf()
    )
}

fun WeatherDto.WeatherDescription.toWeatherUIDescription(): WeatherUI.WeatherUIDescription {
    return WeatherUI.WeatherUIDescription(
        description = main ?: emptyString()
    )
}

private fun convertStringToDate(dateString: String?): Calendar? {
    if (dateString == null) return null
    val millis = dateString.millisToLong() ?: return null
    val calendar = Calendar.getInstance(TimeZone.getDefault())
    calendar.timeInMillis = millis
    return calendar
}