package com.juliana.weatherapp.domain.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException


object DataAccess {
    suspend fun <LocalType, RemoteType> fetchData(
        fetchLocal: suspend () -> LocalType?,
        fetchRemote: suspend () -> RemoteType?,
        saveRemoteData: suspend (RemoteType) -> Unit,
        mapRemoteToLocal: (RemoteType) -> LocalType,
    ): Flow<Resource<LocalType>> = flow {
        emit(Resource.Loading)

        val localData = fetchLocal()
        if (localData != null) {
            emit(Resource.Success(localData))
        }

        when (val remoteResult = apiErrorHandling { fetchRemote() }) {
            is Resource.Success -> {
                val remoteData = remoteResult.data
                if (remoteData != null) {
                    val localMappedData = mapRemoteToLocal(remoteData)
                    saveRemoteData(remoteData)
                    emit(Resource.Success(localMappedData))
                } else {
                    emit(Resource.Error("Failed to load remote data", localData))
                }
            }

            is Resource.Error -> {
                emit(Resource.Error(remoteResult.message, localData))
            }

            is Resource.Loading -> {
                emit(Resource.Loading)
            }
        }
    }

    private suspend fun <T> apiErrorHandling(apiCall: suspend () -> T): Resource<T> {
        return try {
            val result = apiCall()
            Resource.Success(result)
        } catch (e: IOException) {
            Resource.Error("Network error. Please check your connection and try again.", e)
        } catch (e: HttpException) {
            Resource.Error("Server error. Please try again later.", e)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error("An unknown error occurred.", e)
        }
    }

}