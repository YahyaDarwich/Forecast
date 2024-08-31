package com.example.forecast.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.forecast.WeatherApplication
import com.example.forecast.data.WeatherAppRepository
import com.example.forecast.model.CurrentWeather
import com.example.forecast.model.Forecast
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class HomeViewModel(weatherAppRepository: WeatherAppRepository) : ViewModel() {
    var weatherUIState: WeatherUIState by mutableStateOf(WeatherUIState.Loading)

    init {
        viewModelScope.launch {
            weatherUIState = WeatherUIState.Loading
            weatherUIState = try {
                val lat = "34.38586232938427"
                val long = "36.009997289005724"

                WeatherUIState.Success(
                    weatherAppRepository.getCurrentWeather(lat, long),
                    weatherAppRepository.getTodayForecast(lat, long),
                    weatherAppRepository.getUpcomingDaysForecast(lat, long)
                )
            } catch (e: IOException) {
                WeatherUIState.Error
            } catch (e: HttpException) {
                WeatherUIState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as WeatherApplication
                HomeViewModel(application.container.weatherAppRepository)
            }
        }
    }
}

sealed interface WeatherUIState {
    data class Success(
        val currentWeather: CurrentWeather,
        val todayForecast: List<CurrentWeather>,
        val upcomingDaysForecast: Map<String, MutableList<CurrentWeather>>
    ) : WeatherUIState

    object Error : WeatherUIState
    object Loading : WeatherUIState
}