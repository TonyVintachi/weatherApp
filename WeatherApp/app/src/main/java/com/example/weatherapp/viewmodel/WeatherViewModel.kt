package com.example.weatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.NetworkResult
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.model.CurrentWeatherResponse
import com.example.weatherapp.model.ForecastResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val weatherRepository: WeatherRepository = WeatherRepository() // Using default constructor for now
) : ViewModel() {

    // StateFlow for Current Weather
    private val _currentWeather = MutableStateFlow<NetworkResult<CurrentWeatherResponse>>(NetworkResult.Loading)
    val currentWeather: StateFlow<NetworkResult<CurrentWeatherResponse>> = _currentWeather.asStateFlow()

    // StateFlow for 5-Day Forecast
    private val _forecast = MutableStateFlow<NetworkResult<ForecastResponse>>(NetworkResult.Loading)
    val forecast: StateFlow<NetworkResult<ForecastResponse>> = _forecast.asStateFlow()

    // Function to fetch both current weather and forecast
    fun fetchWeatherData(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            // Fetch Current Weather
            _currentWeather.value = NetworkResult.Loading // Set loading state
            weatherRepository.getCurrentWeather(latitude, longitude)
                .catch { e ->
                    _currentWeather.value = NetworkResult.Error("Failed to fetch current weather", e as? Exception)
                }
                .collect { result ->
                    _currentWeather.value = result
                }

            // Fetch Forecast
            _forecast.value = NetworkResult.Loading // Set loading state
            weatherRepository.getFiveDayForecast(latitude, longitude)
                .catch { e ->
                    _forecast.value = NetworkResult.Error("Failed to fetch forecast", e as? Exception)
                }
                .collect { result ->
                    _forecast.value = result
                }
        }
    }

    // Example: Trigger fetch with default coordinates (e.g., London)
    // In a real app, these would come from location services or user input.
    fun fetchWeatherForDefaultLocation() {
        // Default to London coordinates, replace with actual location logic later
        fetchWeatherData(latitude = 51.5074, longitude = 0.1278)
    }
}
