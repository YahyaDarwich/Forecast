package com.example.forecast.network

import com.example.forecast.BuildConfig
import com.example.forecast.model.CurrentWeather
import com.example.forecast.model.Forecast
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("appid") apiK: String = BuildConfig.API_KEY,
        @Query("units") unit: String = "metric"
    ): CurrentWeather

    @GET("forecast")
    suspend fun getForecast(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("appid") apiK: String = BuildConfig.API_KEY,
        @Query("units") unit: String = "metric"
    ): Forecast
}