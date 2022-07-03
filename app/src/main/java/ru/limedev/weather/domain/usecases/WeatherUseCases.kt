package ru.limedev.weather.domain.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.limedev.weather.data.repository.DailyWeatherRepository
import ru.limedev.weather.domain.WeatherResult
import ru.limedev.weather.domain.entity.WeatherRequestEntity
import ru.limedev.weather.domain.mappers.toWeatherViewState
import ru.limedev.weather.presentation.viewstate.WeatherState
import javax.inject.Inject

class WeatherUseCases @Inject constructor(
    private val dailyWeatherRepository: DailyWeatherRepository
) {

    suspend fun fetchWeatherData(weatherRequestEntity: WeatherRequestEntity): Flow<WeatherState> {
        return flow {
            emit(WeatherState.Loading)
            emit(formatWeatherData(dailyWeatherRepository.fetchData(weatherRequestEntity)))
        }.flowOn(Dispatchers.IO)
    }

    private fun <T> formatWeatherData(weatherResult: WeatherResult<T>): WeatherState {
        return weatherResult.toWeatherViewState()
    }
}