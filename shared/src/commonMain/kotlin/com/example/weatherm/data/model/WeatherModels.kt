package com.example.weatherm.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeatherResponse(
    val main: MainData,
    val weather: List<WeatherDescription>,
    val wind: WindData,
    val name: String,
    val coord: Coordinates
)

@Serializable
data class MainData(
    val temp: Double,
    @SerialName("feels_like") val feelsLike: Double,
    val humidity: Int,
    val pressure: Int
)

@Serializable
data class WeatherDescription(
    val main: String,
    val description: String,
    val icon: String
)

@Serializable
data class WindData(
    val speed: Double
)

@Serializable
data class Coordinates(
    val lat: Double,
    val lon: Double
)

@Serializable
data class ForecastResponse(
    val list: List<ForecastItem>
)

@Serializable
data class ForecastItem(
    val dt: Long,
    val main: MainData,
    val weather: List<WeatherDescription>,
    val wind: WindData,
    @SerialName("dt_txt") val dtTxt: String
)

@Serializable
data class GeocodingResponse(
    val name: String,
    val lat: Double,
    val lon: Double,
    val country: String,
    val state: String? = null
)
