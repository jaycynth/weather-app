package com.juliana.weatherapp.domain.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.juliana.weatherapp.WeatherApp.Companion.applicationContext
import dagger.hilt.android.internal.Contexts


fun hasInternetConnection(): Boolean {
    val connectivityManager =
        Contexts.getApplication(applicationContext()).getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
    return when {
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
}