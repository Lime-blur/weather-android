package ru.limedev.weather.domain

import ru.limedev.weather.domain.entity.ErrorType

sealed class WeatherResult<T> {
    data class Success<T>(val value: T) : WeatherResult<T>()
    data class Failure<T>(val error: ErrorType = ErrorType.ERROR_1_UNKNOWN_ERROR) : WeatherResult<T>()
}