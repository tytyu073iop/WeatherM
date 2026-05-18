package com.example.weatherm.data.local

import androidx.room3.RoomDatabase

expect fun getDatabaseBuilder(): RoomDatabase.Builder<CityDatabase>
