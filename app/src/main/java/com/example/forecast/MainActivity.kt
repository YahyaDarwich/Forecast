package com.example.forecast

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.forecast.ui.screens.HomeScreen
import com.example.forecast.ui.screens.HomeViewModel
import com.example.forecast.ui.screens.SearchScreen
import com.example.forecast.ui.screens.SearchViewModel
import com.example.forecast.ui.screens.WeatherUIState
import com.example.forecast.ui.screens.getWeatherColor
import com.example.forecast.ui.theme.ForecastTheme

class MainActivity : ComponentActivity() {
    private val locationPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_DENIED || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_DENIED
        ) {
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }

        setContent {
            ForecastTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)
                    val searchViewModel: SearchViewModel =
                        viewModel(factory = SearchViewModel.Factory)
                    val weatherUIState = homeViewModel.weatherUIState

                    HomeScreen(
                        weatherUIState = weatherUIState,
                        todayWeatherReportUiState = homeViewModel.todayWeatherReportUiState,
                        paddingValues = innerPadding
                    )

                    when (weatherUIState) {
                        is WeatherUIState.Success -> SearchScreen(
                            modifier = Modifier,
                            topPadding = innerPadding.calculateTopPadding(),
                            weatherSearchUiState = searchViewModel.weatherSearchUiState,
                            onLocationClick = { lat, long -> homeViewModel.loadWeather(lat, long) },
                            onSearch = { searchViewModel.onSearch(it) },
                            colors = getWeatherColor(currentWeather = weatherUIState.currentWeather).map {
                                colorResource(
                                    id = it
                                )
                            }
                        )

                        else -> {}
                    }
                }
            }
        }
    }
}