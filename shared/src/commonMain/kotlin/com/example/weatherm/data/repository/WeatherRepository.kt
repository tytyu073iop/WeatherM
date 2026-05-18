package com.example.weatherm.data.repository

import com.example.weatherm.data.local.CityDao
import com.example.weatherm.data.local.CityEntity
import com.example.weatherm.data.model.*
import com.example.weatherm.data.remote.WeatherApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

interface WeatherRepository {
    val savedCities: Flow<List<CityEntity>>
    suspend fun getCurrentWeather(lat: Double, lon: Double): CurrentWeatherResponse
    suspend fun getForecast(lat: Double, lon: Double): ForecastResponse
    suspend fun searchCity(query: String): List<GeocodingResponse>
    suspend fun addCity(city: GeocodingResponse)
    suspend fun removeCity(city: CityEntity)
}

class RealWeatherRepository(
    private val apiService: WeatherApiService,
    private val cityDao: CityDao
) : WeatherRepository {
    override val savedCities: Flow<List<CityEntity>> = cityDao.getAllCities()

    override suspend fun getCurrentWeather(lat: Double, lon: Double): CurrentWeatherResponse {
        return apiService.getCurrentWeather(lat, lon)
    }

    override suspend fun getForecast(lat: Double, lon: Double): ForecastResponse {
        return apiService.getForecast(lat, lon)
    }

    override suspend fun searchCity(query: String): List<GeocodingResponse> {
        return apiService.geocode(query)
    }

    override suspend fun addCity(city: GeocodingResponse) {
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

    override suspend fun removeCity(city: CityEntity) {
        cityDao.deleteCity(city)
    }
}

class FakeWeatherRepository(
    private val apiService: WeatherApiService
) : WeatherRepository {
    private val _cities = MutableStateFlow<List<CityEntity>>(emptyList())
    override val savedCities: Flow<List<CityEntity>> = _cities.asStateFlow()

    override suspend fun getCurrentWeather(lat: Double, lon: Double): CurrentWeatherResponse {
        return apiService.getCurrentWeather(lat, lon)
    }

    override suspend fun getForecast(lat: Double, lon: Double): ForecastResponse {
        return apiService.getForecast(lat, lon)
    }

    override suspend fun searchCity(query: String): List<GeocodingResponse> {
        return apiService.geocode(query)
    }

    override suspend fun addCity(city: GeocodingResponse) {
        _cities.value = _cities.value + CityEntity(
            name = city.name,
            lat = city.lat,
            lon = city.lon,
            country = city.country,
            state = city.state
        )
    }

    override suspend fun removeCity(city: CityEntity) {
        _cities.value = _cities.value.filter { it.name != city.name }
    }
}
