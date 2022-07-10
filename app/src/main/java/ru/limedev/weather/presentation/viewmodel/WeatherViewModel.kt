package ru.limedev.weather.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import ru.limedev.weather.domain.entity.WeatherRequestEntity
import ru.limedev.weather.domain.mappers.toWeatherDbEntity
import ru.limedev.weather.domain.mappers.toWeatherUI
import ru.limedev.weather.domain.usecases.WeatherUseCases
import ru.limedev.weather.presentation.intent.WeatherIntent
import ru.limedev.weather.presentation.lifecycle.SingleLiveEvent
import ru.limedev.weather.presentation.viewstate.WeatherState
import java.util.*
import javax.inject.Inject

private const val MAX_INTERVAL_OLD_DATE = 60000

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherUseCases: WeatherUseCases
) : ViewModel() {

    val weatherIntent = Channel<WeatherIntent>(Channel.UNLIMITED)
    private val _weatherLiveData = SingleLiveEvent<WeatherState>()
    val weatherLiveData = _weatherLiveData

    fun handleIntent() {
        viewModelScope.launch {
            weatherIntent.consumeAsFlow().collect {
                when (it) {
                    is WeatherIntent.FetchDailyWeather -> fetchDailyWeather(it.weatherRequestEntity)
                }
            }
        }
    }

    private suspend fun fetchDailyWeather(weatherRequestEntity: WeatherRequestEntity) {
        weatherUseCases
            .fetchWeatherDbData(weatherRequestEntity.cityType)
            .collectLatest { weatherDbEntity ->
                if (weatherDbEntity == null) {
                    weatherUseCases.fetchWeatherData(weatherRequestEntity).collectLatest {
                        if (it is WeatherState.Success) {
                            weatherUseCases.insertDailyWeatherIntoDb(
                                it.weather.toWeatherDbEntity(Calendar.getInstance().timeInMillis)
                            )
                        }
                        _weatherLiveData.setValue(it)
                    }
                } else {
                    if (detectOldDate(weatherDbEntity.requestDateInMillis)) {
                        weatherUseCases.fetchWeatherData(weatherRequestEntity).collectLatest {
                            if (it is WeatherState.Success) {
                                weatherUseCases.insertDailyWeatherIntoDb(
                                    it.weather.toWeatherDbEntity(Calendar.getInstance().timeInMillis)
                                )
                            }
                            _weatherLiveData.setValue(it)
                        }
                    } else {
                        val state = WeatherState.Success(weatherDbEntity.toWeatherUI())
                        _weatherLiveData.setValue(state)
                    }
                }
            }
    }

    private fun detectOldDate(oldDateInMillis: Long): Boolean {
        val currentDate = Calendar.getInstance().timeInMillis
        return (currentDate - oldDateInMillis > MAX_INTERVAL_OLD_DATE)
    }
}