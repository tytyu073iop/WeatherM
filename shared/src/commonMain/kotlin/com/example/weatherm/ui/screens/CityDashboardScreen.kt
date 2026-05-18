package com.example.weatherm.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weatherm.data.local.CityEntity
import com.example.weatherm.getPlatform
import com.example.weatherm.ui.WeatherViewModel
import com.example.weatherm.ui.components.*

@Composable
fun CityDashboardScreen(
    viewModel: WeatherViewModel,
    onCityClick: (CityEntity) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val platform = getPlatform().name.lowercase()

    Column(modifier = Modifier.fillMaxSize()) {
        if (platform.contains("ios")) {
            IosCitySelector(
                cities = uiState.savedCities.map { it.entity },
                selectedCity = null, // Or handle selected city state
                onCitySelected = { onCityClick(it) }
            )
        }

        AdaptiveSearchField(
            value = searchQuery,
            onValueChange = { 
                searchQuery = it
                if (it.length > 2) viewModel.searchCity(it)
            },
            modifier = Modifier.fillMaxWidth()
        )

        if (uiState.isLoading && uiState.searchResults.isEmpty()) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        if (uiState.searchResults.isNotEmpty()) {
            LazyColumn(modifier = Modifier.fillMaxWidth().heightIn(max = 200.dp)) {
                items(uiState.searchResults) { result ->
                    ListItem(
                        headlineContent = { Text("${result.name}, ${result.country}") },
                        modifier = Modifier.clickable { 
                            viewModel.addCity(result)
                            searchQuery = ""
                        }
                    )
                }
            }
        }

        if (uiState.savedCities.isEmpty() && uiState.isLoading) {
            Column {
                repeat(3) { CityCardSkeleton() }
            }
        } else {
            AdaptiveCityList(
                cities = uiState.savedCities,
                onCityClick = onCityClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
