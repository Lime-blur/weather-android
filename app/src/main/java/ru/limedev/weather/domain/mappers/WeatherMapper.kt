package ru.limedev.weather.domain.mappers

import ru.limedev.weather.domain.WeatherResult
import ru.limedev.weather.domain.entity.WeatherResponseEntity
import ru.limedev.weather.presentation.viewstate.WeatherState

fun <T> WeatherResult<T>.toWeatherViewState(): WeatherState {
    return when(this) {
        is WeatherResult.Success<T> -> WeatherState.Success(value as? WeatherResponseEntity)
        is WeatherResult.Failure<T> -> WeatherState.Error(error)
    }
}