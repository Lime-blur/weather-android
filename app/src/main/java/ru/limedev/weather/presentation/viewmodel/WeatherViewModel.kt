package ru.limedev.weather.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import ru.limedev.weather.domain.entity.WeatherRequestEntity
import ru.limedev.weather.domain.usecases.WeatherUseCases
import ru.limedev.weather.presentation.intent.WeatherIntent
import ru.limedev.weather.presentation.lifecycle.SingleLiveEvent
import ru.limedev.weather.presentation.utilities.getCurrentDateInMillis
import ru.limedev.weather.presentation.utilities.isOldDate
import ru.limedev.weather.presentation.viewstate.WeatherState
import javax.inject.Inject

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
            .collectLatest {
                if (it is WeatherState.Success) {
                    if (it.weather.requestDateInMillis.isOldDate()) {
                        fetchNetworkDailyWeather(weatherRequestEntity)
                    } else {
                        _weatherLiveData.setValue(it)
                    }
                } else {
                    fetchNetworkDailyWeather(weatherRequestEntity)
                }
            }
    }

    private suspend fun fetchNetworkDailyWeather(weatherRequestEntity: WeatherRequestEntity) {
        weatherUseCases.fetchWeatherData(weatherRequestEntity).collectLatest {
            if (it is WeatherState.Success) {
                val currentDateMillis = getCurrentDateInMillis()
                weatherUseCases.insertDailyWeatherIntoDb(it.weather, currentDateMillis)
            }
            _weatherLiveData.setValue(it)
        }
    }
}