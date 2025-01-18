package com.example.forecast.model

import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeather(
    val weather: List<Weather>,
    val main: Temperature,
    val visibility: Int,
    val wind: Wind,
    val rain: Rain? = null,
    val sys: Sys? = null,
    val clouds: Clouds,
    val id: Int,
    val name: String?,
    val dt_txt: String?
)

@Serializable
data class Weather(val id: Int, val main: String, val description: String, val icon: String)

@Serializable
data class Temperature(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int,
    val sea_level: Int,
    val grnd_level: Int
)

@Serializable
data class Wind(val speed: Double, val deg: Int, val gust: Double)

@Serializable
data class Rain(val `1h`: Float = 0f)

@Serializable
data class Clouds(val all: Int)

@Serializable
data class Sys(val country: String?, val sunrise: Long, val sunset: Long)