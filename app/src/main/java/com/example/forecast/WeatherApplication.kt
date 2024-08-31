package com.example.forecast

import android.app.Application
import com.example.forecast.data.AppContainer
import com.example.forecast.data.DefaultAppContainer

class WeatherApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}