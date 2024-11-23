package com.example.forecast.model

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val name: String,
    val lat: Double,
    val lon: Double,
    val country: String,
    val state: String?,
    val local_names: LocalNames
)

@Serializable
data class LocalNames(val en: String)
