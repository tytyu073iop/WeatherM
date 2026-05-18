package com.example.weatherm

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatherm.data.repository.provideWeatherRepository
import com.example.weatherm.ui.WeatherViewModel
import com.example.weatherm.ui.screens.CityDashboardScreen
import com.example.weatherm.ui.screens.WeatherDetailsScreen

@Composable
fun App() {
    MaterialTheme {
        val repository = remember { provideWeatherRepository() }
        val viewModel = viewModel { WeatherViewModel(repository) }
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = "dashboard") {
            composable("dashboard") {
                CityDashboardScreen(
                    viewModel = viewModel,
                    onCityClick = { city ->
                        viewModel.selectCity(city)
                        navController.navigate("details")
                    }
                )
            }
            composable("details") {
                WeatherDetailsScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
