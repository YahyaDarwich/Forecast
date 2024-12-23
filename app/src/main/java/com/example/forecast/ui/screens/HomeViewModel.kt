package com.example.forecast.ui.screens

import android.location.Location
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.forecast.BuildConfig
import com.example.forecast.WeatherApplication
import com.example.forecast.data.LocationRepository
import com.example.forecast.data.WeatherAppPreferencesRepository
import com.example.forecast.data.WeatherAppRepository
import com.example.forecast.model.CurrentWeather
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class HomeViewModel(
    private val weatherAppRepository: WeatherAppRepository,
    private val weatherLocationRepository: LocationRepository,
    private val weatherAppPreferencesRepository: WeatherAppPreferencesRepository
) : ViewModel() {
    var weatherUIState: WeatherUIState by mutableStateOf(WeatherUIState.Loading)

    var todayWeatherReportUiState: TodayWeatherReportUiState by mutableStateOf(
        TodayWeatherReportUiState.Loading
    )

    private lateinit var loadedLat: String
    private lateinit var loadedLong: String

    private val model: GenerativeModel = GenerativeModel(
        "gemini-1.5-flash",
        BuildConfig.geminiApiKey,
        generationConfig = generationConfig {
            maxOutputTokens = 8192
            responseMimeType = "text/plain"
        }, safetySettings = listOf(
            SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.MEDIUM_AND_ABOVE),
            SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.MEDIUM_AND_ABOVE),
            SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.MEDIUM_AND_ABOVE),
            SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.MEDIUM_AND_ABOVE),
        )
    )

    init {
        getCurrentWeatherAndLoad()
    }

    private fun getCurrentWeatherAndLoad() {
        weatherLocationRepository.getCurrentLocation { location ->
            viewModelScope.launch(Dispatchers.IO) {
                getLatLongAndLoad(location)
            }
        }
    }

    private suspend fun getLatLongAndLoad(location: Location?) {
        var lat = weatherAppPreferencesRepository.localLatitude.first()
        var long = weatherAppPreferencesRepository.localLongitude.first()
        if (location != null) {
            lat = location.latitude.toString()
            long = location.longitude.toString()

            weatherAppPreferencesRepository.saveLatitude(lat)
            weatherAppPreferencesRepository.saveLongitude(long)
        }

        loadWeather(lat, long)
    }

    fun reloadLastCalledWeather() {
        if (loadedLat.isNotBlank() && loadedLong.isNotBlank()) {
            loadWeather(loadedLat, loadedLong)
        }
    }


    fun loadWeather(lat: String, long: String) {
        weatherUIState = WeatherUIState.Loading
        todayWeatherReportUiState = TodayWeatherReportUiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            var todayForecast: List<CurrentWeather> = listOf()

            weatherUIState = try {
                loadedLat = lat
                loadedLong = long

                todayForecast = weatherAppRepository.getTodayForecast(lat, long)

                WeatherUIState.Success(
                    weatherAppRepository.getCurrentWeather(lat, long),
                    todayForecast,
                    weatherAppRepository.getUpcomingDaysForecast(lat, long)
                )
            } catch (e: IOException) {
                WeatherUIState.Error
            } catch (e: HttpException) {
                WeatherUIState.Error
            }

            todayWeatherReportUiState = if (todayForecast.isNotEmpty()) {
                try {
                    TodayWeatherReportUiState.Success(model.generateContent("give me summary for today weather from these data $todayForecast").text)
                } catch (e: Exception) {
                    TodayWeatherReportUiState.Error
                }
            } else TodayWeatherReportUiState.Error
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as WeatherApplication
                HomeViewModel(
                    application.container.weatherAppRepository,
                    application.container.weatherLocationRepository,
                    application.container.weatherAppPreferencesRepository
                )
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

    data object Error : WeatherUIState
    data object Loading : WeatherUIState
}

sealed interface TodayWeatherReportUiState {
    data class Success(val summaryReport: String?) : TodayWeatherReportUiState
    data object Error : TodayWeatherReportUiState
    data object Loading : TodayWeatherReportUiState
}