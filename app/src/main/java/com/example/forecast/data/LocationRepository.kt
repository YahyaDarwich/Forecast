package com.example.forecast.data

import android.location.Location
import com.example.forecast.network.LocationHelper

interface LocationRepository {
    fun getCurrentLocation(onSuccess: (Location?) -> Unit)
}

class WeatherLocationRepository(private val locationHelper: LocationHelper) : LocationRepository {
    override fun getCurrentLocation(onSuccess: (Location?) -> Unit) {
        return locationHelper.getCurrentLocation(onSuccess)
    }
}