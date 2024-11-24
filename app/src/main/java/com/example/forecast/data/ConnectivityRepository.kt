package com.example.forecast.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.content.ContextCompat

class ConnectivityRepository(private val context: Context) {
    fun isInternetAvailable(): Boolean {
        var isInternetAvailable = false

        val connectivityManager =
            ContextCompat.getSystemService(context, ConnectivityManager::class.java)

        connectivityManager?.let {
            val network = it.activeNetwork
            val capabilities = it.getNetworkCapabilities(network)

            isInternetAvailable =
                capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
        }

        return isInternetAvailable
    }
}