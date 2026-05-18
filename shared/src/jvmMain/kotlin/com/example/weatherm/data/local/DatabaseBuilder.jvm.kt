package com.example.weatherm.data.local

import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import java.io.File
import kotlinx.coroutines.Dispatchers

actual fun getDatabaseBuilder(): RoomDatabase.Builder<CityDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), "city_db")
    return Room.databaseBuilder<CityDatabase>(
        name = dbFile.absolutePath,
        factory = { CityDatabaseConstructor.initialize() }
    ).setDriver(BundledSQLiteDriver())
     .setQueryCoroutineContext(Dispatchers.IO)
}
