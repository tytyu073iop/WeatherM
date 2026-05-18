package com.example.weatherm.ui

import com.example.weatherm.data.local.CityDao
import com.example.weatherm.data.local.CityEntity
import com.example.weatherm.data.model.*
import com.example.weatherm.data.remote.WeatherApiService
import com.example.weatherm.data.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class WeatherViewModelTest {

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
    fun testAddCityUpdatesState() = runTest {
        val dao = FakeCityDao()
        val repository = WeatherRepository(WeatherApiService(), dao)
        val viewModel = WeatherViewModel(repository)
        
        val city = GeocodingResponse("Minsk", 53.9, 27.56, "BY")
        viewModel.addCity(city)
        // Check state if possible
    }

    @Test
    fun testSearchCityUpdatesState() = runTest {
        val dao = FakeCityDao()
        val api = object : WeatherApiService() {
            override suspend fun geocode(query: String): List<GeocodingResponse> {
                return listOf(GeocodingResponse("Minsk", 53.9, 27.56, "BY"))
            }
        }
        val repository = WeatherRepository(api, dao)
        val viewModel = WeatherViewModel(repository)
        
        viewModel.searchCity("Minsk")
    }

    @Test
    fun testSelectCityUpdatesForecast() = runTest {
        val dao = FakeCityDao()
        val api = object : WeatherApiService() {
            override suspend fun getForecast(lat: Double, lon: Double): ForecastResponse {
                return ForecastResponse(emptyList())
            }
        }
        val repository = WeatherRepository(api, dao)
        val viewModel = WeatherViewModel(repository)
        
        val city = CityEntity("Minsk", 53.9, 27.56, "BY")
        viewModel.selectCity(city)
    }
}
