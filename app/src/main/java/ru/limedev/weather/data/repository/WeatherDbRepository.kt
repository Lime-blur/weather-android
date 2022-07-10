package ru.limedev.weather.data.repository

import ru.limedev.weather.data.db.WeatherDbDao
import ru.limedev.weather.domain.entity.CityType
import ru.limedev.weather.domain.entity.WeatherDbEntity
import ru.limedev.weather.domain.repository.IWeatherDbRepository
import javax.inject.Inject

class WeatherDbRepository @Inject constructor(
    private val weatherDbDao: WeatherDbDao
) : IWeatherDbRepository {

    override suspend fun getDailyWeatherByCityType(cityType: CityType): WeatherDbEntity? {
        return weatherDbDao.getDailyWeatherByCityType(cityType)
    }

    override suspend fun insertDailyWeather(vararg weatherDbEntities: WeatherDbEntity) {
        weatherDbDao.insertDailyWeather(*weatherDbEntities)
    }

    override suspend fun deleteDailyWeather(weatherDbEntities: WeatherDbEntity) {
        weatherDbDao.deleteDailyWeather(weatherDbEntities)
    }
}