package ru.limedev.weather.data.repository

import retrofit2.Response
import ru.limedev.weather.data.model.WeatherDto
import ru.limedev.weather.data.network.RestClient
import ru.limedev.weather.domain.WeatherResult
import ru.limedev.weather.domain.entity.CityType
import ru.limedev.weather.domain.entity.ErrorType
import ru.limedev.weather.domain.entity.ErrorType.Companion.getErrorFromExceptionMessage
import ru.limedev.weather.domain.entity.WeatherRequestEntity
import ru.limedev.weather.domain.entity.WeatherResponseEntity
import ru.limedev.weather.domain.repository.IWeatherRepository
import javax.inject.Inject

class DailyWeatherRepository @Inject constructor(
    private val restClient: RestClient
) : IWeatherRepository<WeatherRequestEntity, WeatherResponseEntity> {

    override suspend fun fetchData(
        request: WeatherRequestEntity
    ): WeatherResult<WeatherResponseEntity> {
        val cityType = request.cityType
        return getResponse(
            cityType = cityType,
            request = {
                restClient.weatherDao?.requestWeather(
                    lat = cityType.lat,
                    lon = cityType.lon,
                    exclude = request.exclude,
                    apiKey = request.apiKey
                )
            },
            defaultErrorMessage = ErrorType.ERROR_0_RESULT_UNSUCCESSFUL
        )
    }

    private suspend fun getResponse(
        cityType: CityType,
        request: suspend () -> Response<WeatherDto>?,
        defaultErrorMessage: ErrorType
    ): WeatherResult<WeatherResponseEntity> {
        return try {
            val result = request.invoke()
            if (result != null && result.isSuccessful) {
                val weatherResponse = WeatherResponseEntity(cityType, result.body())
                WeatherResult.Success(weatherResponse)
            } else {
                WeatherResult.Failure(defaultErrorMessage)
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            WeatherResult.Failure(getErrorFromExceptionMessage(e.message))
        }
    }
}