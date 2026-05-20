package com.example.weatherm.data.remote

import com.example.weatherm.BuildKonfig
import com.example.weatherm.data.model.CurrentWeatherResponse
import com.example.weatherm.data.model.ForecastResponse
import com.example.weatherm.data.model.GeocodingResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

open class WeatherApiService(
    private val client: HttpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            })
        }
    }
) {

    private val apiKey = BuildKonfig.WEATHER_API_KEY
    private val baseUrl = "https://api.openweathermap.org"

    open suspend fun getCurrentWeather(lat: Double, lon: Double): CurrentWeatherResponse {
        return client.get("$baseUrl/data/2.5/weather") {
            parameter("lat", lat)
            parameter("lon", lon)
            parameter("units", "metric")
            parameter("appid", apiKey)
        }.body()
    }

    open suspend fun getForecast(lat: Double, lon: Double): ForecastResponse {
        return client.get("$baseUrl/data/2.5/forecast") {
            parameter("lat", lat)
            parameter("lon", lon)
            parameter("units", "metric")
            parameter("appid", apiKey)
        }.body()
    }

    open suspend fun geocode(query: String): List<GeocodingResponse> {
        return client.get("$baseUrl/geo/1.0/direct") {
            parameter("q", query)
            parameter("limit", 5)
            parameter("appid", apiKey)
        }.body()
    }
}
