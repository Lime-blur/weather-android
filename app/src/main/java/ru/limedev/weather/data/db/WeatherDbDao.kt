package ru.limedev.weather.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.limedev.weather.domain.entity.CityType
import ru.limedev.weather.domain.entity.WeatherDbEntity

@Dao
interface WeatherDbDao {

    @Query("SELECT cityType FROM weatherDbEntity ORDER BY requestDateInMillis DESC LIMIT 1")
    suspend fun getLastSelectedCityType(): CityType?

    @Query("SELECT * FROM weatherDbEntity WHERE cityType = :cityType")
    suspend fun getDailyWeatherByCityType(cityType: CityType): WeatherDbEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyWeather(vararg weatherDbEntities: WeatherDbEntity)

    @Delete
    suspend fun deleteDailyWeather(weatherDbEntities: WeatherDbEntity)
}