package ru.limedev.weather.domain.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.limedev.weather.data.model.WeatherDbDto
import ru.limedev.weather.data.model.WeatherDto

class Converters {

    @TypeConverter
    fun fromWeatherDbDto(weatherDbDto: WeatherDbDto?): String? {
        return Gson().toJson(weatherDbDto)
    }

    @TypeConverter
    fun toWeatherDbDto(weatherDbDtoString: String?): WeatherDbDto? {
        val weatherDbDtoType = object : TypeToken<WeatherDbDto?>(){}.type
        return Gson().fromJson(weatherDbDtoString, weatherDbDtoType)
    }

    @TypeConverter
    fun fromWeatherDbInfo(weatherDbInfo: WeatherDbDto.WeatherDbInfo?): String? {
        return Gson().toJson(weatherDbInfo)
    }

    @TypeConverter
    fun toWeatherDbInfo(weatherDbInfoString: String?): WeatherDbDto.WeatherDbInfo? {
        val weatherDbInfoType = object : TypeToken<WeatherDto.Weather?>(){}.type
        return Gson().fromJson(weatherDbInfoString, weatherDbInfoType)
    }

    @TypeConverter
    fun fromWeatherDbInfoList(weatherDbInfoList: List<WeatherDbDto.WeatherDbInfo>?): String? {
        return Gson().toJson(weatherDbInfoList)
    }

    @TypeConverter
    fun toWeatherDbInfoList(weatherDbInfoString: String?): List<WeatherDbDto.WeatherDbInfo>? {
        val weatherDbInfoType = object : TypeToken<List<WeatherDbDto.WeatherDbInfo>?>(){}.type
        return Gson().fromJson(weatherDbInfoString, weatherDbInfoType)
    }
}