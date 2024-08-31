package com.example.forecast.data

import com.example.forecast.network.WeatherApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val weatherAppRepository: WeatherAppRepository
}

class DefaultAppContainer() : AppContainer {
    private val baseUrl = "https://api.openweathermap.org/data/2.5/"
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
}