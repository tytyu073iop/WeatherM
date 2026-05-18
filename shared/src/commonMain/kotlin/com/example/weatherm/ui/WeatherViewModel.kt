package com.example.weatherm.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherm.data.local.CityEntity
import com.example.weatherm.data.model.CurrentWeatherResponse
import com.example.weatherm.data.model.ForecastResponse
import com.example.weatherm.data.model.GeocodingResponse
import com.example.weatherm.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class WeatherUiState(
    val savedCities: List<CityWeatherState> = emptyList(),
    val searchResults: List<GeocodingResponse> = emptyList(),
    val selectedCityWeather: ForecastResponse? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

data class CityWeatherState(
    val entity: CityEntity,
    val currentWeather: CurrentWeatherResponse? = null
)

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    init {
        observeCities()
    }

    private fun observeCities() {
        viewModelScope.launch {
            repository.savedCities.collectLatest { cities ->
                val cityWeatherStates = cities.map { city ->
                    val weather = try {
                        repository.getCurrentWeather(city.lat, city.lon)
                    } catch (e: Exception) {
                        null
                    }
                    CityWeatherState(city, weather)
                }
                _uiState.value = _uiState.value.copy(savedCities = cityWeatherStates)
            }
        }
    }

    fun searchCity(query: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val results = repository.searchCity(query)
                _uiState.value = _uiState.value.copy(searchResults = results, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message, isLoading = false)
            }
        }
    }

    fun addCity(city: GeocodingResponse) {
        viewModelScope.launch {
            repository.addCity(city)
            _uiState.value = _uiState.value.copy(searchResults = emptyList())
        }
    }

    fun removeCity(city: CityEntity) {
        viewModelScope.launch {
            repository.removeCity(city)
        }
    }

    fun selectCity(city: CityEntity) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val forecast = repository.getForecast(city.lat, city.lon)
                _uiState.value = _uiState.value.copy(selectedCityWeather = forecast, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message, isLoading = false)
            }
        }
    }
}
