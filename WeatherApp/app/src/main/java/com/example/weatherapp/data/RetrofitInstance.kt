package com.example.weatherapp.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

object RetrofitInstance {

    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    // Configure Json to ignore unknown keys and be lenient
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    // Configure OkHttpClient with a logging interceptor
    private val okHttpClient: OkHttpClient by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Log request and response bodies
        }
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS) // Optional: Set connect timeout
            .readTimeout(30, TimeUnit.SECONDS)    // Optional: Set read timeout
            .build()
    }

    // Configure Retrofit instance
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // Use the configured OkHttpClient
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    // Publicly accessible WeatherApiService instance
    val api: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }
}
