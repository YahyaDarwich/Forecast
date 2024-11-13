package com.example.forecast

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.example.forecast.data.AppContainer
import com.example.forecast.data.DefaultAppContainer

class WeatherApplication : Application() {
    private val Context.dataStore by preferencesDataStore(
        name = "${BuildConfig.APPLICATION_ID}_preferences"
    )
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this, dataStore)
    }
}