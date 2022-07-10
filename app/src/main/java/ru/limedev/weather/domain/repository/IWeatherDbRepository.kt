package ru.limedev.weather.domain.repository

import ru.limedev.weather.domain.entity.CityType
import ru.limedev.weather.domain.entity.WeatherDbEntity

interface IWeatherDbRepository {
    suspend fun getDailyWeatherByCityType(cityType: CityType): WeatherDbEntity?
    suspend fun insertDailyWeather(vararg weatherDbEntities: WeatherDbEntity)
    suspend fun deleteDailyWeather(weatherDbEntities: WeatherDbEntity)
}