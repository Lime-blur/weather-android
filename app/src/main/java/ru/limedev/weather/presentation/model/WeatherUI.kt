package ru.limedev.weather.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.limedev.weather.domain.entity.CityType
import java.util.*

@Parcelize
data class WeatherUI(
    val cityType: CityType,
    val currentWeather: WeatherUIInfo,
    val dailyWeather: List<WeatherUIInfo>
) : Parcelable {

    @Parcelize
    data class WeatherUIInfo(
        val date: Calendar?,
        val weatherList: List<WeatherUIDescription>
    ) : Parcelable

    @Parcelize
    data class WeatherUIDescription(
        val description: String
    ) : Parcelable
}