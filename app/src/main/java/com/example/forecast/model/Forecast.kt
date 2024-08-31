package com.example.forecast.model

import kotlinx.serialization.Serializable

@Serializable
data class Forecast(val list: List<CurrentWeather>, val city: City)

@Serializable
data class City(
    val id: Int,
    val name: String,
    val population: Int,
    val sunrise: Long,
    val sunset: Long
)