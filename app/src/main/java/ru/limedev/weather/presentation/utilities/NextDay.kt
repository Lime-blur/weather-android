package ru.limedev.weather.presentation.utilities

enum class NextDay(val offset: Int) {
    TOMORROW(0),
    ONE_DAY_AFTER_TOMORROW(1),
    TWO_DAYS_AFTER_TOMORROW(2);
}