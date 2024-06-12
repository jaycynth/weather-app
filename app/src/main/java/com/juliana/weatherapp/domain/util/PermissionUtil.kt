package com.juliana.weatherapp.domain.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager

fun Context.isLocationPermissionGranted(): Boolean {
    return this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
}