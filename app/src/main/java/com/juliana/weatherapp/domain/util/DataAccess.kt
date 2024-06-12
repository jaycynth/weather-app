package com.juliana.weatherapp.domain.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException


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

        val remoteResult = apiErrorHandling { fetchRemote() }
        when (remoteResult) {
            is Resource.Success -> {
                val remoteData = remoteResult.data
                if (remoteData != null) {
                    val localMappedData = mapRemoteToLocal(remoteData)
                    saveRemoteData(remoteData)
                    emit(Resource.Success(localMappedData))
                } else {
                    emit(Resource.Error("No data found", localData))
                }
            }

            is Resource.Error -> {
                emit(Resource.Error(remoteResult.message, localData))
            }

            Resource.Loading -> {
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
            Resource.Error("Something went wrong. Try again.", e)
        } catch (e: UnknownHostException) {
            Resource.Error("Network error. Please check your connection and try again.", e)
        } catch (e: Exception) {
            Resource.Error("Unknown error occurred", e)
        }
    }
}

