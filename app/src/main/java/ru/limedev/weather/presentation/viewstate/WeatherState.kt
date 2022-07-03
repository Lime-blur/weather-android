package ru.limedev.weather.presentation.viewstate

import ru.limedev.weather.domain.entity.ErrorType
import ru.limedev.weather.domain.entity.WeatherResponseEntity

sealed class WeatherState {

    object NoState : WeatherState()

    object Loading : WeatherState()

    data class Success(
        val data: WeatherResponseEntity?
    ) : WeatherState()

    data class Error(
        val error: ErrorType = ErrorType.ERROR_1_UNKNOWN_ERROR
    ) : WeatherState()
}