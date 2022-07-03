package ru.limedev.weather.domain.repository

import ru.limedev.weather.domain.WeatherResult

interface IWeatherRepository<T, R> {
    suspend fun fetchData(request: T): WeatherResult<R>
}