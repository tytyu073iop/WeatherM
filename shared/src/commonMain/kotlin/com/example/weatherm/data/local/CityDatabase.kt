package com.example.weatherm.data.local

import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.RoomDatabaseConstructor

@Database(entities = [CityEntity::class], version = 1)
@ConstructedBy(CityDatabaseConstructor::class)
abstract class CityDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao
}

// The expected companion object for Room KMP
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object CityDatabaseConstructor : RoomDatabaseConstructor<CityDatabase>

fun getRoomDatabase(
    builder: RoomDatabase.Builder<CityDatabase>
): CityDatabase {
    return builder
        .fallbackToDestructiveMigration(true)
        .build()
}
