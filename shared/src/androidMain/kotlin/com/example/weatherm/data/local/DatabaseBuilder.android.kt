package com.example.weatherm.data.local

import android.content.Context
import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers

actual fun getDatabaseBuilder(): RoomDatabase.Builder<CityDatabase> {
    val appContext = AppContext.get()
    val dbFile = appContext.getDatabasePath("city_db")
    return Room.databaseBuilder<CityDatabase>(
        context = appContext,
        name = dbFile.absolutePath,
        factory = { CityDatabaseConstructor.initialize() }
    ).setDriver(BundledSQLiteDriver())
     .setQueryCoroutineContext(Dispatchers.IO)
}

object AppContext {
    private lateinit var applicationContext: Context
    fun set(context: Context) {
        applicationContext = context
    }
    fun get(): Context = applicationContext
}
