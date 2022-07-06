package ru.limedev.weather.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.limedev.weather.BuildConfig
import ru.limedev.weather.databinding.ActivityMainBinding
import ru.limedev.weather.domain.entity.CityType
import ru.limedev.weather.domain.entity.ErrorType
import ru.limedev.weather.domain.entity.WeatherRequestEntity
import ru.limedev.weather.presentation.intent.WeatherIntent
import ru.limedev.weather.presentation.model.WeatherUI
import ru.limedev.weather.presentation.viewmodel.WeatherViewModel
import ru.limedev.weather.presentation.viewstate.WeatherState

private const val SAVED_DAILY_WEATHER_KEY = "SAVED_DAILY_WEATHER_KEY"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val weatherViewModel: WeatherViewModel by viewModels()
    private var savedWeather: WeatherUI? = null

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observeWeatherData()
        loadWeatherData(savedInstanceState)
        weatherViewModel.handleIntent()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        savedWeather?.let {
            outState.putParcelable(SAVED_DAILY_WEATHER_KEY, it)
        }
        super.onSaveInstanceState(outState)
    }

    private fun loadWeatherData(savedInstanceState: Bundle?) {
        val weatherUI = savedInstanceState?.getParcelable<WeatherUI>(SAVED_DAILY_WEATHER_KEY)
        if (weatherUI != null) {
            handleSuccessState(weatherUI)
        } else {
            requestWeatherData()
        }
    }

    private fun requestWeatherData() {
        weatherViewModel.viewModelScope.launch {
            val weatherRequestEntity = WeatherRequestEntity(
                cityType = CityType.GOMEL,
                apiKey = BuildConfig.API_KEY
            )
            weatherViewModel.weatherIntent.send(
                WeatherIntent.FetchDailyWeather(weatherRequestEntity)
            )
        }
    }

    private fun observeWeatherData() {
        weatherViewModel.weatherLiveData.observe(this, { weatherState ->
            when (weatherState) {
                is WeatherState.Loading -> handleLoadingState()
                is WeatherState.NoState -> handleNoState()
                is WeatherState.Error -> handleErrorState(weatherState.error)
                is WeatherState.Success -> handleSuccessState(weatherState.weather)
            }
        })
    }

    private fun handleErrorState(errorType: ErrorType) {
        savedWeather = null
        Log.e("Error", getString(errorType.resId))
    }

    private fun handleNoState() {
        savedWeather = null
    }

    private fun handleLoadingState() {
        savedWeather = null
    }

    private fun handleSuccessState(weatherUI: WeatherUI?) {
        savedWeather = weatherUI
        Log.i("Success", weatherUI.toString())
    }
}