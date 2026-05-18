package com.example.weatherm.data.repository

import com.example.weatherm.data.local.getDatabaseBuilder
import com.example.weatherm.data.local.getRoomDatabase
import com.example.weatherm.data.remote.WeatherApiService

actual fun provideWeatherRepository(): WeatherRepository {
    val database = getRoomDatabase(getDatabaseBuilder())
    return RealWeatherRepository(WeatherApiService(), database.cityDao())
}
