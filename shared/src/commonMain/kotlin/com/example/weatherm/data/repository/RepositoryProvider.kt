package com.example.weatherm.data.repository

import com.example.weatherm.data.local.getDatabaseBuilder
import com.example.weatherm.data.local.getRoomDatabase
import com.example.weatherm.data.remote.WeatherApiService

expect fun provideWeatherRepository(): WeatherRepository
