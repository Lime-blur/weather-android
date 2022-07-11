package ru.limedev.weather.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.limedev.weather.data.model.WeatherDto
import ru.limedev.weather.domain.converters.Converters
import ru.limedev.weather.domain.entity.WeatherDbEntity

private const val DB_VERSION = 1

@Database(entities = [WeatherDbEntity::class], version = DB_VERSION)
@TypeConverters(Converters::class)
abstract class WeatherDb : RoomDatabase() {
    abstract fun getWeatherDbDao(): WeatherDbDao
}