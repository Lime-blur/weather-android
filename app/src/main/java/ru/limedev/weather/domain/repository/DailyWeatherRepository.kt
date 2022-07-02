package ru.limedev.weather.domain.repository

import retrofit2.Response
import ru.limedev.weather.data.model.WeatherDto
import ru.limedev.weather.domain.entity.CityType
import ru.limedev.weather.domain.entity.ErrorType
import ru.limedev.weather.domain.entity.WeatherResponseEntity
import ru.limedev.weather.presentation.viewState.WeatherResponseState

class DailyWeatherRepository(
    private val defaultErrorMessage: ErrorType
) {

    suspend fun getDailyWeather(
        cityType: CityType,
        request: suspend () -> Response<WeatherDto>?
    ): WeatherResponseState {
        return try {
            val result = request.invoke()
            if (result != null && result.isSuccessful) {
                val weatherResponse = WeatherResponseEntity(cityType, result.body())
                WeatherResponseState.Success(weatherResponse)
            } else {
                WeatherResponseState.Error(defaultErrorMessage)
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            WeatherResponseState.Error(
                ErrorType.getErrorFromExceptionMessage(e.message)
            )
        }
    }
}