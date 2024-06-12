package com.juliana.weatherapp.domain.util



sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val message: String, val data: Any? = null) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}
