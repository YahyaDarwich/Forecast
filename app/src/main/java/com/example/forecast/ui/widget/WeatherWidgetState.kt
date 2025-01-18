package com.example.forecast.ui.widget

import com.example.forecast.model.CurrentWeather
import kotlinx.serialization.Serializable

@Serializable
sealed interface WeatherWidgetState {
    @Serializable
    data object Loading : WeatherWidgetState

    @Serializable
    data class Success(val currentWeather: CurrentWeather) : WeatherWidgetState

    @Serializable
    data class Error(val message: String) : WeatherWidgetState
}