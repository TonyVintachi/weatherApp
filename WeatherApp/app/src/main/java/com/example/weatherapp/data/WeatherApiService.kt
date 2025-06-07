package com.example.weatherapp.data

import com.example.weatherapp.model.CurrentWeatherResponse
import com.example.weatherapp.model.ForecastResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    companion object {
        // It's good practice to store the API key securely,
        // e.g., in local.properties, and access via BuildConfig.
        // For now, it's directly here but acknowledge this is not ideal for production.
        const val API_KEY = "c9f2c3ef48dac53498e89a085d8618ba"
    }

    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String = API_KEY,
        @Query("units") units: String = "metric" // Or "imperial"
    ): CurrentWeatherResponse

    @GET("forecast")
    suspend fun getFiveDayForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String = API_KEY,
        @Query("units") units: String = "metric" // Or "imperial"
    ): ForecastResponse
}
