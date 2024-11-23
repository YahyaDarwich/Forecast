package com.example.forecast.network

import com.example.forecast.BuildConfig
import com.example.forecast.model.CurrentWeather
import com.example.forecast.model.Forecast
import com.example.forecast.model.Location
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("appid") apiK: String = BuildConfig.API_KEY,
        @Query("units") unit: String = "metric"
    ): CurrentWeather

    @GET("data/2.5/forecast")
    suspend fun getForecast(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("appid") apiK: String = BuildConfig.API_KEY,
        @Query("units") unit: String = "metric"
    ): Forecast

    @GET("geo/1.0/direct")
    suspend fun searchLocation(
        @Query("q") name: String,
        @Query("limit") limit: String = "5",
        @Query("appid") apiK: String = BuildConfig.API_KEY
    ): List<Location>
}