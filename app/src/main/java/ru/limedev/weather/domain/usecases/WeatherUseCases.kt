package ru.limedev.weather.domain.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.limedev.weather.data.repository.WeatherDbRepository
import ru.limedev.weather.data.repository.WeatherNetworkRepository
import ru.limedev.weather.domain.WeatherResult
import ru.limedev.weather.domain.entity.CityType
import ru.limedev.weather.domain.entity.WeatherDbEntity
import ru.limedev.weather.domain.entity.WeatherRequestEntity
import ru.limedev.weather.domain.mappers.toWeatherViewState
import ru.limedev.weather.presentation.viewstate.WeatherState
import javax.inject.Inject

class WeatherUseCases @Inject constructor(
    private val weatherNetworkRepository: WeatherNetworkRepository,
    private val weatherDbRepository: WeatherDbRepository
) {

    suspend fun fetchWeatherData(
        weatherRequestEntity: WeatherRequestEntity
    ): Flow<WeatherState> {
        return flow {
            emit(WeatherState.Loading)
            emit(formatWeatherData(weatherNetworkRepository.fetchData(weatherRequestEntity)))
        }.flowOn(Dispatchers.IO)
    }

    suspend fun fetchWeatherDbData(cityType: CityType): Flow<WeatherDbEntity?> {
        return flow {
            emit(weatherDbRepository.getDailyWeatherByCityType(cityType))
        }.flowOn(Dispatchers.IO)
    }

    suspend fun insertDailyWeatherIntoDb(weatherDbEntity: WeatherDbEntity?) {
        if (weatherDbEntity == null) return
        weatherDbRepository.insertDailyWeather(weatherDbEntity)
    }

    private fun <T> formatWeatherData(weatherResult: WeatherResult<T>): WeatherState {
        return weatherResult.toWeatherViewState()
    }
}