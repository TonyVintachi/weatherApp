package com.example.weatherapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Coordinates(
    val lon: Double,
    val lat: Double
)

@Serializable
data class WeatherCondition(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

@Serializable
data class MainDetails(
    val temp: Double,
    @SerialName("feels_like")
    val feelsLike: Double,
    @SerialName("temp_min")
    val tempMin: Double,
    @SerialName("temp_max")
    val tempMax: Double,
    val pressure: Int,
    val humidity: Int,
    @SerialName("sea_level")
    val seaLevel: Int? = null, // Optional
    @SerialName("grnd_level")
    val groundLevel: Int? = null // Optional
)

@Serializable
data class WindDetails(
    val speed: Double,
    val deg: Int,
    val gust: Double? = null // Optional
)

@Serializable
data class CloudsDetails(
    val all: Int // Cloudiness percentage
)

@Serializable
data class RainDetails(
    @SerialName("1h")
    val oneHour: Double? = null, // Optional: Rain volume for the last 1 hour
    @SerialName("3h")
    val threeHour: Double? = null // Optional: Rain volume for the last 3 hours
)

@Serializable
data class SnowDetails(
    @SerialName("1h")
    val oneHour: Double? = null, // Optional: Snow volume for the last 1 hour
    @SerialName("3h")
    val threeHour: Double? = null // Optional: Snow volume for the last 3 hours
)

@Serializable
data class SysInfo(
    val type: Int? = null, // Optional
    val id: Int? = null,   // Optional
    val country: String? = null, // Optional
    val sunrise: Long, // Sunrise time, unix, UTC
    val sunset: Long   // Sunset time, unix, UTC
)
