package com.juliana.weatherapp.domain.util

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


object DataAccess {

    suspend fun <LocalType, RemoteType> fetchData(
        fetchLocal: suspend () -> LocalType?,
        fetchRemote: suspend () -> RemoteType?,
        saveRemoteData: suspend (RemoteType) -> Unit,
        mapRemoteToLocal: (RemoteType) -> LocalType,
    ): Flow<Resource<LocalType>> = flow {

        emit(Resource.Loading)

        val localData = fetchLocal()
        localData?.let {
            emit(Resource.Success(localData))
        }


        if (hasInternetConnection()) {
            val remoteResult = apiErrorHandling { fetchRemote() }
            when (remoteResult) {
                is Resource.Success -> {
                    val remoteData = remoteResult.data
                    if (remoteData != null) {
                        val localMappedData = mapRemoteToLocal(remoteData)
                        saveRemoteData(remoteData)
                        emit(Resource.Success(localMappedData))
                    } else {
                        emit(Resource.Error("No remote data found"))
                    }
                }

                is Resource.Error -> {
                    emit(Resource.Error(remoteResult.message))
                }

                Resource.Loading -> {
                    emit(Resource.Loading)
                }
            }
        } else {
            emit(Resource.Error("No Internet"))
        }
    }

    private suspend fun <T> apiErrorHandling(apiCall: suspend () -> T): Resource<T> {
        return try {
            val result = apiCall()
            Resource.Success(result)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "An error occurred")
        }
    }
}

