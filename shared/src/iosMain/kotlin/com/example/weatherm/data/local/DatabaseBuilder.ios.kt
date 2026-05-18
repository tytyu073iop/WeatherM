package com.example.weatherm.data.local

import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import platform.Foundation.NSHomeDirectory
import kotlinx.coroutines.Dispatchers

actual fun getDatabaseBuilder(): RoomDatabase.Builder<CityDatabase> {
    val dbFilePath = NSHomeDirectory() + "/city_db"
    return Room.databaseBuilder<CityDatabase>(
        name = dbFilePath,
        factory = { CityDatabaseConstructor.initialize() }
    ).setDriver(BundledSQLiteDriver())
     .setQueryCoroutineContext(Dispatchers.Main) // iOS might prefer Main or a specific background dispatcher
}
