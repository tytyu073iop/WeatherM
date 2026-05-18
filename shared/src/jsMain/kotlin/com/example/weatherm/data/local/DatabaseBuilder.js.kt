package com.example.weatherm.data.local

import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.driver.web.WebWorkerSQLiteDriver
import org.w3c.dom.Worker

actual fun getDatabaseBuilder(): RoomDatabase.Builder<CityDatabase> {
    return Room.databaseBuilder<CityDatabase>(
        name = "city_db",
        factory = { CityDatabaseConstructor.initialize() }
    ).setDriver(WebWorkerSQLiteDriver(
        worker = Worker("sqlite-worker.js")
    ))
}
