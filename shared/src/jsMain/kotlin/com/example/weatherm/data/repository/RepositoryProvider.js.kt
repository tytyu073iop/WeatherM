package com.example.weatherm.data.repository

import com.example.weatherm.data.remote.WeatherApiService

actual fun provideWeatherRepository(): WeatherRepository {
    return FakeWeatherRepository(WeatherApiService())
}
