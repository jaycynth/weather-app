package com.juliana.weatherapp

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WeatherApp: Application(){
    init {
        instance = this
    }

    companion object {
        private var instance: WeatherApp? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }

    }
}