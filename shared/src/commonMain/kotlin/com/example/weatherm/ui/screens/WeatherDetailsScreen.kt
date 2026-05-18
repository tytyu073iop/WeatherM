package com.example.weatherm.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weatherm.ui.WeatherViewModel
import com.example.weatherm.ui.components.AdaptiveWeatherCard
import coil3.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherDetailsScreen(
    viewModel: WeatherViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val forecast = uiState.selectedCityWeather

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(forecast?.list?.firstOrNull()?.dtTxt ?: "Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (forecast == null) {
                CircularProgressIndicator()
            } else {
                val current = forecast.list.firstOrNull()
                current?.let {
                    Text(it.main.temp.toInt().toString() + "°C", style = MaterialTheme.typography.displayLarge)
                    Text(it.weather.firstOrNull()?.description ?: "", style = MaterialTheme.typography.headlineMedium)
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        WeatherMetric("Humidity", "${it.main.humidity}%")
                        WeatherMetric("Wind", "${it.wind.speed} m/s")
                        WeatherMetric("Pressure", "${it.main.pressure} hPa")
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Text("5-Day Forecast", style = MaterialTheme.typography.headlineSmall)
                    
                    LazyRow {
                        items(forecast.list.filter { it.dtTxt.contains("12:00:00") }) { item ->
                            AdaptiveWeatherCard(modifier = Modifier.width(120.dp)) {
                                Column(
                                    modifier = Modifier.padding(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(item.dtTxt.substringBefore(" ").substring(5))
                                    AsyncImage(
                                        model = "https://openweathermap.org/img/wn/${item.weather.first().icon}@2x.png",
                                        contentDescription = null,
                                        modifier = Modifier.size(48.dp)
                                    )
                                    Text("${item.main.temp.toInt()}°C")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherMetric(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.labelMedium)
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}
