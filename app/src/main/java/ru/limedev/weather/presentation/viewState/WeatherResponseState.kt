package ru.limedev.weather.presentation.viewState

import ru.limedev.weather.domain.entity.ErrorType
import ru.limedev.weather.domain.entity.WeatherResponseEntity

sealed class WeatherResponseState {

    object NoState : WeatherResponseState()

    object Loading : WeatherResponseState()

    data class Success(
        val data: WeatherResponseEntity
    ) : WeatherResponseState()

    data class Error(
        val error: ErrorType = ErrorType.ERROR_1_UNKNOWN_ERROR
    ) : WeatherResponseState()
}