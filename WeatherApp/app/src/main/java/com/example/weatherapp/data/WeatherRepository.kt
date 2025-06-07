package com.example.weatherapp.data

import com.example.weatherapp.model.CurrentWeatherResponse
import com.example.weatherapp.model.ForecastResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

// A sealed class to represent UI states, particularly for data loading
sealed class NetworkResult<out T> {
    data class Success<out T>(val data: T) : NetworkResult<T>()
    data class Error(val message: String, val exception: Exception? = null) : NetworkResult<Nothing>()
    object Loading : NetworkResult<Nothing>()
}

class WeatherRepository {

    private val weatherApiService: WeatherApiService = RetrofitInstance.api

    // Fetches current weather and emits results as a Flow
    fun getCurrentWeather(latitude: Double, longitude: Double): Flow<NetworkResult<CurrentWeatherResponse>> = flow {
        emit(NetworkResult.Loading) // Emit Loading state
        try {
            val response = weatherApiService.getCurrentWeather(latitude, longitude)
            emit(NetworkResult.Success(response)) // Emit Success with data
        } catch (e: Exception) {
            // More specific error handling can be done here (e.g., differentiate
            // between network errors, HTTP errors, parsing errors)
            emit(NetworkResult.Error("Failed to fetch current weather: ${e.message}", e))
        }
    }.flowOn(Dispatchers.IO) // Execute network call on IO dispatcher

    // Fetches 5-day forecast and emits results as a Flow
    fun getFiveDayForecast(latitude: Double, longitude: Double): Flow<NetworkResult<ForecastResponse>> = flow {
        emit(NetworkResult.Loading) // Emit Loading state
        try {
            val response = weatherApiService.getFiveDayForecast(latitude, longitude)
            emit(NetworkResult.Success(response)) // Emit Success with data
        } catch (e: Exception) {
            emit(NetworkResult.Error("Failed to fetch 5-day forecast: ${e.message}", e))
        }
    }.flowOn(Dispatchers.IO) // Execute network call on IO dispatcher
}
