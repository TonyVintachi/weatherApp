package com.example.weatherapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeatherResponse(
    @SerialName("coord")
    val coordinates: Coordinates,
    val weather: List<WeatherCondition>,
    val base: String, // Internal parameter
    val main: MainDetails,
    val visibility: Int,
    val wind: WindDetails,
    val clouds: CloudsDetails,
    val rain: RainDetails? = null, // Optional
    val snow: SnowDetails? = null, // Optional
    val dt: Long, // Time of data calculation, unix, UTC
    val sys: SysInfo,
    val timezone: Int, // Shift in seconds from UTC
    val id: Int, // City ID
    val name: String, // City name
    val cod: Int // Internal parameter
)
