package com.example.forecast.data

import com.example.forecast.model.CurrentWeather
import com.example.forecast.model.Location
import com.example.forecast.network.WeatherApiService
import com.example.forecast.utils.DateHelper

interface WeatherAppRepository {
    suspend fun getCurrentWeather(lat: String, long: String): CurrentWeather
    suspend fun getTodayForecast(lat: String, long: String): List<CurrentWeather>
    suspend fun getUpcomingDaysForecast(
        lat: String,
        long: String
    ): Map<String, MutableList<CurrentWeather>>

    suspend fun searchLocation(name: String): List<Location>
}

class NetworkWeatherAppRepository(private val weatherApiService: WeatherApiService) :
    WeatherAppRepository {
    override suspend fun getCurrentWeather(lat: String, long: String): CurrentWeather =
        weatherApiService.getCurrentWeather(lat, long)

    override suspend fun getTodayForecast(lat: String, long: String): List<CurrentWeather> {
        val forecast = weatherApiService.getForecast(lat, long)
        val todayForecast: MutableList<CurrentWeather> = mutableListOf()

        for (weather in forecast.list) {
            if (DateHelper.isDateToday(weather.dt_txt)) {
                todayForecast.add(weather)
            }
        }

        return todayForecast.toList()
    }

    override suspend fun getUpcomingDaysForecast(
        lat: String,
        long: String
    ): Map<String, MutableList<CurrentWeather>> {
        val forecast = weatherApiService.getForecast(lat, long)
        val upcomingDaysForecast: MutableMap<String, MutableList<CurrentWeather>> = mutableMapOf()
        var weatherHour: String
        var key: String

        for (weather in forecast.list) {
            weatherHour = DateHelper.formatDate(weather.dt_txt, outputPattern = "h a")
            if (weatherHour == "12 pm" || weatherHour == "9 pm") {
                key = DateHelper.formatDate(weather.dt_txt, outputPattern = "yyyy-MM-dd")
                if (!upcomingDaysForecast.containsKey(key)) {
                    upcomingDaysForecast[key] = mutableListOf(weather)
                } else {
                    val list: MutableList<CurrentWeather>? = upcomingDaysForecast[key]
                    list?.add(weather)
                    upcomingDaysForecast[key] = list?.toMutableList() ?: mutableListOf()
                }
            }
        }

        return upcomingDaysForecast
    }

    override suspend fun searchLocation(name: String): List<Location> = weatherApiService.searchLocation(name)
}