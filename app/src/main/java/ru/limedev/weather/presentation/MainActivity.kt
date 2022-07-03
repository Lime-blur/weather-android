package ru.limedev.weather.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.limedev.weather.BuildConfig
import ru.limedev.weather.R
import ru.limedev.weather.domain.entity.CityType
import ru.limedev.weather.domain.entity.WeatherRequestEntity
import ru.limedev.weather.presentation.intent.WeatherIntent
import ru.limedev.weather.presentation.viewmodel.WeatherViewModel
import ru.limedev.weather.presentation.viewstate.WeatherState

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val weatherViewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getNewsData()
        weatherViewModel.viewModelScope.launch {
            val weatherRequestEntity = WeatherRequestEntity(
                cityType = CityType.GOMEL,
                apiKey = BuildConfig.API_KEY
            )
            weatherViewModel.weatherIntent.send(
                WeatherIntent.FetchDailyWeather(weatherRequestEntity)
            )
        }
        weatherViewModel.handleIntent()
    }

    private fun getNewsData() {
        weatherViewModel.weatherLiveData.observe(this, { newsState ->
            when (newsState) {
                is WeatherState.Loading -> {}
                is WeatherState.NoState -> {}
                is WeatherState.Error -> {
                    Log.e("Error", getString(newsState.error.resId))
                }
                is WeatherState.Success -> {
                    Log.i("Success", newsState.data.toString())
                }
            }
        })
    }
}