package com.example.forecast.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.map


class WeatherAppPreferencesRepository(private val dataStore: DataStore<Preferences>) {
    companion object PreferencesKeys {
        val LATITUDE = stringPreferencesKey("latitude")
        val LONGITUDE = stringPreferencesKey("longitude")
    }

    val localLatitude = dataStore.data.map { preferences ->
        preferences[LATITUDE] ?: ""
    }
    val localLongitude = dataStore.data.map { preferences ->
        preferences[LONGITUDE] ?: ""
    }

    suspend fun saveLatitude(latitude: String) {
        dataStore.edit { preferences ->
            preferences[LATITUDE] = latitude
        }
    }

    suspend fun saveLongitude(longitude: String) {
        dataStore.edit { preferences ->
            preferences[LONGITUDE] = longitude
        }
    }
}