package com.example.weatherm.data.local

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "cities")
data class CityEntity(
    @PrimaryKey val name: String,
    val lat: Double,
    val lon: Double,
    val country: String,
    val state: String? = null
)
