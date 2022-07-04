package ru.limedev.weather.presentation.viewstate

import ru.limedev.weather.domain.entity.ErrorType
import ru.limedev.weather.domain.entity.WeatherResponseEntity
import ru.limedev.weather.presentation.model.WeatherUI

sealed class WeatherState {

    object NoState : WeatherState()

    object Loading : WeatherState()

    data class Success(
        val weather: WeatherUI?
    ) : WeatherState()

    data class Error(
        val error: ErrorType = ErrorType.ERROR_1_UNKNOWN_ERROR
    ) : WeatherState()
}