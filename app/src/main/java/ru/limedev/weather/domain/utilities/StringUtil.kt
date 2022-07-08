package ru.limedev.weather.domain.utilities

fun String.millisToLong(): Long? {
    return try {
        this.toLong()
    } catch (e: NumberFormatException) {
        e.printStackTrace()
        null
    }
}

fun emptyString() = ""