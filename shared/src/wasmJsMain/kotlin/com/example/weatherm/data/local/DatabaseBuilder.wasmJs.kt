package com.example.weatherm.data.local

import androidx.room3.Room
import androidx.room3.RoomDatabase

actual fun getDatabaseBuilder(): RoomDatabase.Builder<CityDatabase> {
    return Room.databaseBuilder<CityDatabase>(
        name = "city_db",
        factory = { CityDatabaseConstructor.initialize() }
    )
}
