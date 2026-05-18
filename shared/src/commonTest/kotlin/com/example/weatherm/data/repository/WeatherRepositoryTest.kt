package com.example.weatherm.data.repository

import com.example.weatherm.data.local.CityDao
import com.example.weatherm.data.local.CityEntity
import com.example.weatherm.data.model.*
import com.example.weatherm.data.remote.WeatherApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class WeatherRepositoryTest {

    private class FakeCityDao : CityDao {
        private val cities = MutableStateFlow<List<CityEntity>>(emptyList())
        override fun getAllCities(): Flow<List<CityEntity>> = cities
        override suspend fun insertCity(city: CityEntity) {
            cities.value = cities.value + city
        }
        override suspend fun deleteCity(city: CityEntity) {
            cities.value = cities.value.filter { it.name != city.name }
        }
    }

    @Test
    fun testAddAndRemoveCity() = runTest {
        val dao = FakeCityDao()
        val repository = WeatherRepository(WeatherApiService(), dao)
        
        val city = GeocodingResponse("Minsk", 53.9, 27.56, "BY")
        repository.addCity(city)
        
        val saved = repository.savedCities.first()
        assertEquals(1, saved.size)
        assertEquals("Minsk", saved[0].name)
        
        repository.removeCity(saved[0])
        assertTrue(repository.savedCities.first().isEmpty())
    }

    @Test
    fun testGetCurrentWeather() = runTest {
        val mockApi = object : WeatherApiService() {
            override suspend fun getCurrentWeather(lat: Double, lon: Double): CurrentWeatherResponse {
                return CurrentWeatherResponse(
                    main = MainData(20.0, 18.0, 50, 1013),
                    weather = listOf(WeatherDescription("Clear", "clear sky", "01d")),
                    wind = WindData(5.0),
                    name = "Minsk",
                    coord = Coordinates(lat, lon)
                )
            }
        }
        val repository = WeatherRepository(mockApi, FakeCityDao())
        val weather = repository.getCurrentWeather(53.9, 27.56)
        assertEquals("Minsk", weather.name)
        assertEquals(20.0, weather.main.temp)
    }

    @Test
    fun testGetForecast() = runTest {
        val mockApi = object : WeatherApiService() {
            override suspend fun getForecast(lat: Double, lon: Double): ForecastResponse {
                return ForecastResponse(listOf(
                    ForecastItem(0, MainData(20.0, 18.0, 50, 1013), emptyList(), WindData(5.0), "2023-10-27 12:00:00")
                ))
            }
        }
        val repository = WeatherRepository(mockApi, FakeCityDao())
        val forecast = repository.getForecast(53.9, 27.56)
        assertEquals(1, forecast.list.size)
    }
}
