package ru.limedev.weather.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.limedev.weather.data.model.WeatherDbDto

@Entity
data class WeatherDbEntity(
    @PrimaryKey
    val cityType: CityType,
    val requestDateInMillis: Long,
    val weather: WeatherDbDto?
)