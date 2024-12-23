package com.example.forecast.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import coil.network.HttpException
import com.example.forecast.WeatherApplication
import com.example.forecast.data.WeatherAppRepository
import com.example.forecast.model.Location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okio.IOException

class SearchViewModel(
    private val weatherAppRepository: WeatherAppRepository
) : ViewModel() {
    var weatherSearchUiState: WeatherSearchUiState by mutableStateOf(WeatherSearchUiState.Loading)
    private lateinit var loadedLocation: String

    init {
        loadedLocation = ""
    }

    fun onSearch(value: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (value.isNotBlank() && value.trim().length > 2) {
                try {
                    if (value.trim() != loadedLocation) {
                        val locations = weatherAppRepository.searchLocation(value.trim())

                        weatherSearchUiState = if (locations.isNotEmpty()) {
                            loadedLocation = value.trim()
                            WeatherSearchUiState.Success(locations)
                        } else WeatherSearchUiState.Loading
                    }
                } catch (e: IOException) {
                    weatherSearchUiState = WeatherSearchUiState.Error
                } catch (e: HttpException) {
                    weatherSearchUiState = WeatherSearchUiState.Error
                }
            } else {
                weatherSearchUiState = WeatherSearchUiState.Loading
                loadedLocation = ""
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as WeatherApplication
                SearchViewModel(application.container.weatherAppRepository)
            }
        }
    }
}

sealed interface WeatherSearchUiState {
    data class Success(val searchResult: List<Location>) : WeatherSearchUiState
    data object Error : WeatherSearchUiState
    data object Loading : WeatherSearchUiState
}