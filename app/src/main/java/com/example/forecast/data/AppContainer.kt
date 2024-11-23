package com.example.forecast.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.forecast.network.LocationHelper
import com.example.forecast.network.WeatherApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val weatherAppRepository: WeatherAppRepository
    val weatherLocationRepository: LocationRepository
    val weatherAppPreferencesRepository: WeatherAppPreferencesRepository
}

class DefaultAppContainer(
    private val context: Context,
    private val dataStore: DataStore<Preferences>
) : AppContainer {
    private val baseUrl = "https://api.openweathermap.org/"
    private val okhttpClient = OkHttpClient.Builder().build()
    private val retrofit: Retrofit =
        Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(baseUrl)
            .client(okhttpClient)
            .build()

    private val retrofitService: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }

    override val weatherAppRepository: WeatherAppRepository by lazy {
        NetworkWeatherAppRepository(retrofitService)
    }

    override val weatherLocationRepository: LocationRepository by lazy {
        WeatherLocationRepository(LocationHelper(context))
    }

    override val weatherAppPreferencesRepository: WeatherAppPreferencesRepository by lazy {
        WeatherAppPreferencesRepository(dataStore)
    }
}