package com.example.weatherm

import com.example.weatherm.data.local.CityDao
import com.example.weatherm.data.local.CityEntity
import com.example.weatherm.data.model.*
import com.example.weatherm.data.remote.WeatherApiService
import com.example.weatherm.data.repository.RealWeatherRepository
import com.example.weatherm.data.repository.WeatherRepository
import com.example.weatherm.ui.WeatherViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class WeatherIntegrationTest {
    
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

    private class MockWeatherApi : WeatherApiService() {
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

    @Test
    fun testAddCityAndFetchWeather() = runTest {
        val dao = FakeCityDao()
        val api = MockWeatherApi()
        val repository = RealWeatherRepository(api, dao)
        val viewModel = WeatherViewModel(repository)

        val city = GeocodingResponse("Minsk", 53.9, 27.56, "BY")
        viewModel.addCity(city)
    }

    @Test
    fun testRemoveCityUpdatesViewModel() = runTest {
        val dao = FakeCityDao()
        val repository = RealWeatherRepository(MockWeatherApi(), dao)
        val viewModel = WeatherViewModel(repository)
        
        val city = CityEntity("Minsk", 53.9, 27.56, "BY")
        dao.insertCity(city)
    }

    @Test
    fun testSelectCityAndLoadForecast() = runTest {
        val dao = FakeCityDao()
        val api = MockWeatherApi()
        val repository = RealWeatherRepository(api, dao)
        val viewModel = WeatherViewModel(repository)

        val city = CityEntity("Minsk", 53.9, 27.56, "BY")
        viewModel.selectCity(city)
    }
}
