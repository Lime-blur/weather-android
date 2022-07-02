package ru.limedev.weather.domain.usecases

import ru.limedev.weather.data.network.RestClient
import ru.limedev.weather.domain.entity.ErrorType
import ru.limedev.weather.domain.entity.WeatherRequestEntity
import ru.limedev.weather.domain.repository.DailyWeatherRepository
import ru.limedev.weather.presentation.viewState.WeatherResponseState
import javax.inject.Inject

class WeatherUseCases(
    @Inject val restClient: RestClient
) {

    suspend fun fetchDailyWeatherData(
        weatherRequestEntity: WeatherRequestEntity
    ): WeatherResponseState {
        val cityType = weatherRequestEntity.cityType
        val dailyWeatherRepository = DailyWeatherRepository(ErrorType.ERROR_0_RESULT_UNSUCCESSFUL)
        return dailyWeatherRepository.getDailyWeather(
            cityType = cityType,
            request = {
                restClient.weatherDao?.requestWeather(
                    lat = cityType.lat,
                    lon = cityType.lon,
                    exclude = weatherRequestEntity.exclude,
                    apiKey = weatherRequestEntity.apiKey
                )
            }
        )
    }
}