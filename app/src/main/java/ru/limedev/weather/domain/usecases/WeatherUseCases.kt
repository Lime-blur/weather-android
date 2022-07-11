package ru.limedev.weather.domain.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.limedev.weather.data.repository.WeatherDbRepository
import ru.limedev.weather.data.repository.WeatherNetworkRepository
import ru.limedev.weather.domain.entity.CityType
import ru.limedev.weather.domain.entity.WeatherRequestEntity
import ru.limedev.weather.domain.mappers.toWeatherDbEntity
import ru.limedev.weather.domain.mappers.toWeatherViewState
import ru.limedev.weather.presentation.model.WeatherUI
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
            emit(weatherNetworkRepository.fetchData(weatherRequestEntity)
                .toWeatherViewState()
            )
        }.flowOn(Dispatchers.IO)
    }

    suspend fun fetchWeatherDbData(cityType: CityType): Flow<WeatherState> {
        return flow {
            emit(weatherDbRepository.getDailyWeatherByCityType(cityType)
                .toWeatherViewState()
            )
        }.flowOn(Dispatchers.IO)
    }

    suspend fun insertDailyWeatherIntoDb(weatherUI: WeatherUI?, currentDateMillis: Long) {
        if (weatherUI == null) return
        val weatherDbEntity = weatherUI.toWeatherDbEntity(currentDateMillis)
        weatherDbRepository.insertDailyWeather(weatherDbEntity)
    }
}