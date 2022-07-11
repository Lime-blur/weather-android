package ru.limedev.weather.domain.mappers

import ru.limedev.weather.data.model.WeatherDbDto
import ru.limedev.weather.data.model.WeatherDto
import ru.limedev.weather.domain.WeatherResult
import ru.limedev.weather.domain.entity.ErrorType
import ru.limedev.weather.domain.entity.WeatherDbEntity
import ru.limedev.weather.domain.entity.WeatherResponseEntity
import ru.limedev.weather.domain.utilities.millisToLong
import ru.limedev.weather.presentation.model.WeatherUI
import ru.limedev.weather.presentation.viewstate.WeatherState
import java.util.*

fun <T> WeatherResult<T>.toWeatherViewState(): WeatherState {
    return when(this) {
        is WeatherResult.Success<T> -> {
            val weatherResponseEntity = value as? WeatherResponseEntity
            if (weatherResponseEntity == null) {
                WeatherState.Error(ErrorType.ERROR_5_ENTITY_FIELD_IS_NULL)
            } else {
                WeatherState.Success(weatherResponseEntity.toWeatherUI())
            }
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

fun WeatherUI.toWeatherDbEntity(
    requestDateInMillis: Long
): WeatherDbEntity {
    return WeatherDbEntity(
        requestDateInMillis = requestDateInMillis,
        cityType = cityType,
        weather = WeatherDbDto(
            currentWeather = currentWeather.toWeatherDbInfo(),
            dailyWeather = dailyWeather.map { it.toWeatherDbInfo() }
        )
    )
}

fun WeatherUI.WeatherUIInfo.toWeatherDbInfo(): WeatherDbDto.WeatherDbInfo {
    return WeatherDbDto.WeatherDbInfo(
        description = description
    )
}

fun WeatherDbEntity?.toWeatherViewState(): WeatherState {
    return if (this == null) {
        WeatherState.Error(ErrorType.ERROR_5_ENTITY_FIELD_IS_NULL)
    } else {
        WeatherState.Success(
            WeatherUI(
                cityType = cityType,
                currentWeather = weather?.currentWeather.toWeatherUIInfo(),
                dailyWeather = weather?.dailyWeather?.map { it.toWeatherUIInfo() } ?: listOf(),
                requestDateInMillis = requestDateInMillis
            )
        )
    }
}

fun WeatherDbDto.WeatherDbInfo?.toWeatherUIInfo(): WeatherUI.WeatherUIInfo {
    return WeatherUI.WeatherUIInfo(
        date = null,
        description = this?.description
    )
}

fun WeatherDto.Weather?.toWeatherUIInfo(): WeatherUI.WeatherUIInfo {
    return WeatherUI.WeatherUIInfo(
        date = convertStringToDate(this?.dt),
        description = this?.weather?.get(Day.TODAY.offset)?.main
    )
}

private fun convertStringToDate(dateString: String?): Calendar? {
    if (dateString == null) return null
    val millis = dateString.millisToLong() ?: return null
    val calendar = Calendar.getInstance(TimeZone.getDefault())
    calendar.timeInMillis = millis
    return calendar
}