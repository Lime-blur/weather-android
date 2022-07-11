package ru.limedev.weather.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.limedev.weather.BuildConfig
import ru.limedev.weather.R
import ru.limedev.weather.databinding.ActivityMainBinding
import ru.limedev.weather.domain.entity.CityType
import ru.limedev.weather.domain.entity.ErrorType
import ru.limedev.weather.domain.entity.WeatherRequestEntity
import ru.limedev.weather.presentation.custom.WeatherSpinnerAdapter
import ru.limedev.weather.presentation.intent.WeatherIntent
import ru.limedev.weather.presentation.model.WeatherUI
import ru.limedev.weather.presentation.utilities.getCurrentWeather
import ru.limedev.weather.presentation.utilities.getNextThreeDaysWeather
import ru.limedev.weather.presentation.viewmodel.WeatherViewModel
import ru.limedev.weather.presentation.viewstate.WeatherState

private const val SAVED_DAILY_WEATHER_KEY = "SAVED_DAILY_WEATHER_KEY"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val weatherViewModel: WeatherViewModel by viewModels()
    private var savedWeather: WeatherUI? = null
    private var spinnerAdapter: WeatherSpinnerAdapter? = null

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupSpinner()
        observeWeatherData()
        loadWeatherData(savedInstanceState)
        weatherViewModel.handleIntent()
        initListeners()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        savedWeather?.let {
            outState.putParcelable(SAVED_DAILY_WEATHER_KEY, it)
        }
        super.onSaveInstanceState(outState)
    }

    private fun initListeners() {
        binding.buttonGetWeather.setOnClickListener {
            val selectedCityType = binding.spinnerCities.selectedItem as? CityType
            if (selectedCityType != null) {
                val requestEntity = buildRequestEntity(selectedCityType)
                requestWeatherData(requestEntity)
            }
        }
    }

    private fun setupSpinner() {
        val adapter = WeatherSpinnerAdapter(this, R.layout.item_spinner)
        adapter.submitArray(CityType.values())
        binding.spinnerCities.adapter = adapter
        spinnerAdapter = adapter
    }

    private fun loadWeatherData(savedInstanceState: Bundle?) {
        val weatherUI = savedInstanceState?.getParcelable<WeatherUI>(SAVED_DAILY_WEATHER_KEY)
        if (weatherUI != null) {
            handleSuccessState(weatherUI)
        } else {
            requestLastSelectedCityType()
        }
    }

    private fun buildRequestEntity(cityType: CityType? = null): WeatherRequestEntity {
        return WeatherRequestEntity(
            cityType = cityType ?: CityType.MINSK,
            apiKey = BuildConfig.API_KEY
        )
    }

    private fun requestWeatherData(weatherRequestEntity: WeatherRequestEntity) {
        weatherViewModel.viewModelScope.launch {
            weatherViewModel.weatherIntent.send(
                WeatherIntent.FetchDailyWeather(weatherRequestEntity)
            )
        }
    }

    private fun requestLastSelectedCityType() {
        weatherViewModel.viewModelScope.launch {
            weatherViewModel.weatherIntent.send(WeatherIntent.FetchLastSelectedCityType)
        }
    }

    private fun observeWeatherData() {
        weatherViewModel.weatherLiveData.observe(this, { weatherState ->
            when (weatherState) {
                is WeatherState.Loading -> handleLoadingState()
                is WeatherState.NoState -> handleNoState()
                is WeatherState.Error -> handleErrorState(weatherState.error)
                is WeatherState.Success -> handleSuccessState(weatherState.weather)
                is WeatherState.SuccessCityType -> handleSuccessCityTypeState(weatherState.cityType)
            }
        })
    }

    private fun handleErrorState(errorType: ErrorType) {
        binding.progressBar.isVisible = false
        Toast.makeText(applicationContext, errorType.resId, Toast.LENGTH_LONG).show()
    }

    private fun handleNoState() {
        savedWeather = null
        binding.progressBar.isVisible = false
    }

    private fun handleLoadingState() {
        savedWeather = null
        binding.progressBar.isVisible = true
    }

    private fun handleSuccessState(weatherUI: WeatherUI) {
        savedWeather = weatherUI
        binding.progressBar.isVisible = false
        binding.tvWeatherNow.text = weatherUI.getCurrentWeather(this)
        binding.tvWeatherNextDays.text = weatherUI.getNextThreeDaysWeather(this)
    }

    private fun handleSuccessCityTypeState(cityType: CityType?) {
        val requestEntity = buildRequestEntity(cityType)
        requestWeatherData(requestEntity)
        if (cityType == null) return
        val position = spinnerAdapter?.getPosition(cityType) ?: return
        binding.spinnerCities.setSelection(position)
    }
}