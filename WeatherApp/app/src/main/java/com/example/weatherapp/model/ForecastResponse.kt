package com.example.weatherapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastListItem(
    val dt: Long, // Time of data forecasted, unix, UTC
    val main: MainDetails,
    val weather: List<WeatherCondition>,
    val clouds: CloudsDetails,
    val wind: WindDetails,
    val visibility: Int,
    @SerialName("pop")
    val probabilityOfPrecipitation: Double, // Probability of precipitation
    val rain: RainDetails? = null,
    val snow: SnowDetails? = null,
    @SerialName("dt_txt")
    val dateTimeText: String // Data/time of calculation, UTC
)

@Serializable
data class CityInfo(
    val id: Int,
    val name: String,
    @SerialName("coord")
    val coordinates: Coordinates,
    val country: String,
    val population: Int,
    val timezone: Int,
    val sunrise: Long,
    val sunset: Long
)

@Serializable
data class ForecastResponse(
    val cod: String, // Internal parameter
    val message: Int, // Internal parameter
    val cnt: Int, // Number of timestamps returned
    val list: List<ForecastListItem>,
    val city: CityInfo
)
