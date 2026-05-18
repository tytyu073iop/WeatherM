package com.example.weatherm.data.repository

import com.example.weatherm.data.local.CityDao
import com.example.weatherm.data.local.CityEntity
import com.example.weatherm.data.model.CurrentWeatherResponse
import com.example.weatherm.data.model.ForecastResponse
import com.example.weatherm.data.model.GeocodingResponse
import com.example.weatherm.data.remote.WeatherApiService
import kotlinx.coroutines.flow.Flow

class WeatherRepository(
    private val apiService: WeatherApiService,
    private val cityDao: CityDao
) {
    val savedCities: Flow<List<CityEntity>> = cityDao.getAllCities()

    suspend fun getCurrentWeather(lat: Double, lon: Double): CurrentWeatherResponse {
        return apiService.getCurrentWeather(lat, lon)
    }

    suspend fun getForecast(lat: Double, lon: Double): ForecastResponse {
        return apiService.getForecast(lat, lon)
    }

    suspend fun searchCity(query: String): List<GeocodingResponse> {
        return apiService.geocode(query)
    }

    suspend fun addCity(city: GeocodingResponse) {
        cityDao.insertCity(
            CityEntity(
                name = city.name,
                lat = city.lat,
                lon = city.lon,
                country = city.country,
                state = city.state
            )
        )
    }

    suspend fun removeCity(city: CityEntity) {
        cityDao.deleteCity(city)
    }
}
