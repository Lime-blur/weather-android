package ru.limedev.weather.presentation.utilities

import android.content.Context
import ru.limedev.weather.R
import ru.limedev.weather.presentation.model.WeatherUI
import java.util.*

private const val MAX_INTERVAL_OLD_DATE = 60000

fun WeatherUI.getNextThreeDaysWeather(context: Context): String {
    return context.getString(
        R.string.weather_next_three_days,
        getNextDayWeather(context, NextDay.TOMORROW),
        getNextDayWeather(context, NextDay.ONE_DAY_AFTER_TOMORROW),
        getNextDayWeather(context, NextDay.TWO_DAYS_AFTER_TOMORROW)
    )
}

fun WeatherUI.getNextDayWeather(context: Context, nextDay: NextDay): String {
    return dailyWeather[nextDay.offset].description ?: context.getString(R.string.unknown_weather)
}

fun WeatherUI.getCurrentWeather(context: Context): String {
    val currentCity = context.getString(cityType.cityName)
    val currentWeather = currentWeather.description ?: context.getString(R.string.unknown_weather)
    return context.getString(R.string.weather_now, currentCity, currentWeather)
}

fun Long.isOldDate(): Boolean {
    val currentDate = getCurrentDateInMillis()
    return (currentDate - this > MAX_INTERVAL_OLD_DATE)
}

fun getCurrentDateInMillis(): Long {
    return Calendar.getInstance(TimeZone.getDefault()).timeInMillis
}