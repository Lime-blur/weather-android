package ru.limedev.weather.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
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
import ru.limedev.weather.presentation.viewstate.WeatherState
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherUseCases: WeatherUseCases
) : ViewModel() {

    val weatherIntent = Channel<WeatherIntent>(Channel.UNLIMITED)
    private val _weatherLiveData = MutableLiveData<WeatherState>()
    val weatherLiveData = _weatherLiveData

    fun handleIntent() {
        viewModelScope.launch {
            weatherIntent.consumeAsFlow().collect {
                when (it) {
                    is WeatherIntent.FetchDailyWeather -> fetchWeather(it.weatherRequestEntity)
                }
            }
        }
    }

    private suspend fun fetchWeather(weatherRequestEntity: WeatherRequestEntity) {
        weatherUseCases.fetchWeatherData(weatherRequestEntity).collectLatest {
            _weatherLiveData.value = it
        }
    }
}